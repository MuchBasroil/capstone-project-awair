import os
from flask import Flask, request, jsonify
import joblib
import numpy as np
import pandas as pd

# Load model and LabelEncoder
model = joblib.load('FixModel-rf.joblib')
label_encoder = joblib.load('label_encoder.joblib')  # Ensure that the LabelEncoder is saved during training

# Load dataset from the URL or local file
dataset_path = 'https://storage.googleapis.com/bungkit-awairs/air_quality_data_fix.csv'
dataframe = pd.read_csv(dataset_path)

# Features used by the model
features = ['universal_aqi', 'co', 'no2', 'o3', 'pm10', 'pm25', 'so2', 'latitude', 'longitude']

# Check if 'health_advice' exists in the DataFrame
health_advice_column_exists = 'health_advice' in dataframe.columns
city_column_exists = 'City' in dataframe.columns

app = Flask(__name__)

@app.route('/predict-rf', methods=['POST'])
def predict():
    try:
        # Get input from the request
        data = request.get_json()

        # Validate the input structure
        if not data or "input" not in data:
            return jsonify({'error': 'Invalid input format, missing "input" field'}), 400
        
        input_data = data['input']

        # Validate the input length matches the expected number of features
        if len(input_data) != len(features):
            return jsonify({'error': f'Input length must be {len(features)} values'}), 400

        # Make prediction
        input_array = np.array(input_data).reshape(1, -1)
        prediction_encoded = model.predict(input_array)
        dominant_pollutant = label_encoder.inverse_transform(prediction_encoded)[0]

        # Match input values with the dataset to get health_general_population
        input_dict = dict(zip(features, input_data))
        
        # Efficient matching of input data with the dataframe
        matching_row = dataframe.loc[
            (dataframe[features] == pd.Series(input_dict)).all(axis=1)
        ]
        
        if not matching_row.empty:
            # Retrieve health_general_population
            health_general_population = matching_row['health_general_population'].values[0]
            # Check if health_advice exists before accessing it
            health_advice = matching_row['health_advice'].values[0] if health_advice_column_exists else "No health advice available."
            
            # Retrieve the concentration of the dominant pollutant
            pollutant_content = matching_row[dominant_pollutant].values[0] if dominant_pollutant in matching_row.columns else "Unknown concentration"
            # Retrieve the city information if available
            city = matching_row['City'].values[0] if city_column_exists else "Unknown city"
        else:
            health_general_population = "Unknown (no match found in dataset)"
            health_advice = "No specific health advice available for the provided data."
            pollutant_content = "Unknown concentration"
            city = "Unknown city"

        # Prepare and send the response
        response = {
            "prediction": {
                "Dom. Pollutant": f"{dominant_pollutant}: {pollutant_content:.2f}",
                "Health Advices": health_general_population,
                "City": city
            }
        }
        return jsonify(response)

    except KeyError as e:
        return jsonify({'error': f'Missing key: {str(e)}'}), 400
    except ValueError as e:
        return jsonify({'error': str(e)}), 400
    except Exception as e:
        return jsonify({'error': 'An error occurred during prediction', 'details': str(e)}), 500

if __name__ == "__main__":
    port = int(os.environ.get('PORT', 8080))  # Use the PORT environment variable if available
    app.run(debug=True, host='0.0.0.0', port=port)