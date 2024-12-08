from flask import Flask, request, jsonify
import joblib
import numpy as np

# Load model .joblib
model = joblib.load('rf-model.joblib')

app = Flask(__name__)

@app.route('/predict-rf', methods=['POST'])
def predict():
    try:
        data = request.get_json()
        input_data = np.array(data['input']).reshape(1, -1)
        prediction = model.predict(input_data)
        return jsonify({'prediction': prediction.tolist()})
    except Exception as e:
        return jsonify({'error': str(e)}), 400

if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8080)
