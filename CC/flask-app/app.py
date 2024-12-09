from flask import Flask, request, jsonify
import joblib
import numpy as np

# Load model dan LabelEncoder
model = joblib.load('FixModel-rf.joblib')
label_encoder = joblib.load('label_encoder.joblib')

# Fitur yang digunakan oleh model
features = ['universal_aqi', 'co', 'no2', 'o3', 'pm10', 'pm25', 'so2', 'latitude', 'longitude']

app = Flask(__name__)

@app.route('/predict-rf', methods=['POST'])
def predict():
    try:
        data = request.get_json()
        if "input" not in data:
            return jsonify({'error': 'Invalid input format, missing "input" field'}), 400

        # Validasi panjang input
        input_data = data['input']
        if len(input_data) != len(features):
            return jsonify({'error': f'Input length must be {len(features)}'}), 400

        # Prediksi model
        input_array = np.array(input_data).reshape(1, -1)
        prediction_encoded = model.predict(input_array)
        dominant_pollutant = label_encoder.inverse_transform(prediction_encoded)[0]

        # Ambil kandungan dominan
        pollutant_index = features.index(dominant_pollutant)
        pollutant_content = input_data[pollutant_index]

        # Respons JSON
        response = {
            "prediction": {
                "dominant_pollutant": dominant_pollutant,
                "content": pollutant_content
            }
        }
        return jsonify(response)
    except Exception as e:
        return jsonify({'error': str(e)}), 400

if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8080)
