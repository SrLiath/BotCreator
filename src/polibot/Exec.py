#############################################################################################################
# Para usar o bot, você terá que executar o comando "python Exec.py -b (aqui fica o nome do bot executado)" #
# o script fica ouvindo o none.voice na pasta vozes, futuramente irei implementar para ouvir todos em vozes #
#############################################################################################################
import os
import subprocess
import vosk
import pyaudio
import threading
import queue
import argparse
from multiprocessing import freeze_support

# Diretório onde os arquivos .txt estão armazenados
DIR_TXT = "../../src/polibot/vozes/"

# Nome do arquivo .txt contendo as palavras-chave
KEYWORDS_FILE = "None.voice"

# Configuração da biblioteca PyAudio para captura de áudio
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 16000
CHUNK_SIZE = 512
BUFFER_SIZE = 8*CHUNK_SIZE
audio = pyaudio.PyAudio()
audio_buffer = queue.Queue(maxsize=int(BUFFER_SIZE/CHUNK_SIZE))

# Processa os argumentos passados via linha de comando
parser = argparse.ArgumentParser(description="Voice control for Java bot")
parser.add_argument("-b", "--bot", type=str, required=True, help="Name of the bot jar file")
args = parser.parse_args()

# Caminho completo para o arquivo .jar do bot
BOT_FILE = os.path.join("../../bot/", args.bot + ".jar")
EXEC = f"java -jar {BOT_FILE}"aadd

# Carrega as palavras-chave do arquivo .txt em uma lista
with open(os.path.join(DIR_TXT, KEYWORDS_FILE), "r") as f:
    keywords = f.read().splitlines()

# Configuração do modelo de reconhecimento de voz
model = vosk.Model("../../src/polibot/model-br")
rec = vosk.KaldiRecognizer(model, 16000)

# Função que verifica se a palavra-chave foi encontrada
def check_keyword(result):
    if any(keyword in result for keyword in keywords):
        subprocess.call(EXEC, shell=True)

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

if __name__ == '__main__':
    freeze_support()
    # Loop principal para manter o programa ouvindo continuamente sem espaços em branco
    while True:
        pass