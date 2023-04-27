import os
import subprocess
import vosk
import pyaudio

# Diretório onde os arquivos .txt estão armazenados
DIR_TXT = "vozes/"

# Nome do arquivo .txt contendo as palavras-chave
KEYWORDS_FILE = "none.voice"

# Caminho completo para o arquivo .bat a ser executado
BAT_FILE = "teste.bat"

# Carrega as palavras-chave do arquivo .txt em uma lista
with open(os.path.join(DIR_TXT, KEYWORDS_FILE), "r") as f:
    keywords = f.read().splitlines()

# Configuração do modelo de reconhecimento de voz
model = vosk.Model("model-br")
rec = vosk.KaldiRecognizer(model, 16000)

# Configuração da biblioteca PyAudio para captura de áudio
audio = pyaudio.PyAudio()
stream = audio.open(format=pyaudio.paInt16, channels=1, rate=16000, input=True, frames_per_buffer=8000)

# Monitora o microfone em busca das palavras-chave
while True:
    print("Ouvindo...")
    data = stream.read(8000)
    if rec.AcceptWaveform(data):
        result = rec.Result()
        if any(keyword in result for keyword in keywords):
            subprocess.call(BAT_FILE, shell=True)
