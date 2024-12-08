# Gunakan image dasar Python
FROM python:3.9-slim

# Set working directory di dalam container
WORKDIR /app

# Copy semua file model dan kode aplikasi
COPY . /app

# Install dependensi yang diperlukan
RUN pip install --no-cache-dir -r requirements.txt

# Expose port yang akan digunakan oleh Cloud Run
EXPOSE 8080

# Menjalankan aplikasi
CMD ["python", "app.py"]
