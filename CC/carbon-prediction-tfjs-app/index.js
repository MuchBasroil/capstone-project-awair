const tf = require('@tensorflow/tfjs-node');
const express = require('express');
const cors = require('cors');
const fs = require('fs');
const path = require('path');
const app = express();

const linearModelURL = 'https://storage.googleapis.com/bungkit-awairs/model_tfjs_linear2/model.json';
const lstmModelURL = 'https://storage.googleapis.com/bungkit-awairs/model_tfjs_lstm/model.json';

let linearModel;
let lstmModel;

// Load the scaler parameters (mean and scale)
const scalerPath = path.join(__dirname, 'scaler.json');
let scalerParams = { mean: [], scale: [] };
try {
  const data = fs.readFileSync(scalerPath, 'utf8');
  scalerParams = JSON.parse(data);
} catch (err) {
  console.error('Error loading scaler parameters:', err);
}

// Middleware
app.use(cors()); // Mengizinkan CORS
app.use(express.json()); // Untuk parsing application/json

// Function to scale input data
const scaleInput = (input) => {
  return input.map((value, index) => (value - scalerParams.mean[index]) / scalerParams.scale[index]);
};

app.post('/predict-linear', async (req, res) => {
  try {
    if (!linearModel) {
      console.log('Loading Linear Regression model...');
      linearModel = await tf.loadGraphModel(linearModelURL);
    }
    // Get the input data and scale it
    const input = req.body.input;
    const scaledInput = scaleInput(input); // Apply scaling to input

    const inputTensor = tf.tensor([scaledInput]); // Add batch dimension
    const prediction = linearModel.predict(inputTensor);
    res.json({ prediction: prediction.arraySync() });
  } catch (error) {
    console.error(error);
    res.status(500).send({ error: error.toString() });
  }
});

// Endpoint untuk prediksi dengan model LSTM
app.post('/predict-lstm', async (req, res) => {
  try {
    if (!lstmModel) {
      console.log('Loading LSTM model...');
      lstmModel = await tf.loadGraphModel(lstmModelURL);
    }

    const inputTensor = tf.tensor(req.body.input, [1, 5, 6]); // Pastikan dimensi input sesuai
    const outputs = lstmModel.outputs;
    const outputName = outputs[0].name;
    const prediction = await lstmModel.executeAsync({ [outputName]: inputTensor }, [outputName]);

    if (prediction instanceof tf.Tensor) {
      res.json({ prediction: prediction.arraySync() });
    } else {
      res.json({ prediction });
    }
  } catch (error) {
    console.error(error);
    res.status(500).send({ error: error.toString() });
  }
});

// Menjalankan server
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});