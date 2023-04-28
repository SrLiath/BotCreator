import os
import subprocess
import vosk
import pyaudio
import threading
import queue

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
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 16000
CHUNK_SIZE = 512
BUFFER_SIZE = 8*CHUNK_SIZE
audio = pyaudio.PyAudio()
audio_buffer = queue.Queue(maxsize=int(BUFFER_SIZE/CHUNK_SIZE))

# Função que verifica se a palavra-chave foi encontrada
def check_keyword(result):
    if any(keyword in result for keyword in keywords):
        subprocess.call(BAT_FILE, shell=True)
        stream.stop_stream()
        stream.close()
        audio.terminate()
        audio = pyaudio.PyAudio()
        stream = audio.open(format=FORMAT, channels=CHANNELS, rate=RATE, input=True, frames_per_buffer=CHUNK_SIZE)
        thread_listen = threading.Thread(target=listen)
        thread_listen.start()

# Função que processa os dados de áudio em paralelo
def process_audio():
    while True:
        data = audio_buffer.get()
        if rec.AcceptWaveform(data):
            result = rec.Result()
            check_keyword(result)

# Cria e inicia a thread para processar o áudio em paralelo
thread_process = threading.Thread(target=process_audio)
thread_process.start()

# Função que ouve o microfone e faz a captura de áudio
def listen():
    stream = audio.open(format=FORMAT, channels=CHANNELS, rate=RATE, input=True, frames_per_buffer=CHUNK_SIZE)
    while True:
        data = stream.read(CHUNK_SIZE)
        audio_buffer.put(data)

# Cria e inicia a thread para ouvir o microfone e fazer a captura de áudio
thread_listen = threading.Thread(target=listen)
thread_listen.start()
