from vosk import Model, KaldiRecognizer
import os
import pyaudio

# Validacao da pasta de modelo
# Eh necessario criar a pasta model-br a partir de onde estiver este fonte
if not os.path.exists("model-br"):
    print ("Modelo em portugues nao encontrado.")
    exit (1)

# Preparando o microfone para captura
p = pyaudio.PyAudio()
stream = p.open(format=pyaudio.paInt16, channels=1, rate=16000, input=True, frames_per_buffer=8000)
stream.start_stream()

# Apontando o algoritmo para ler o modelo treinado na pasta "model-br"
model = Model("model-br")
rec = KaldiRecognizer(model, 16000)

# Variável para armazenar a frase reconhecida
recognized_phrase = ""

# Criando um loop continuo para ficar ouvindo o microfone
while True:
    # Lendo audio do microfone
    data = stream.read(8000)

    # Convertendo audio em texto
    if rec.AcceptWaveform(data):
        result = rec.Result()
        print(result)
        if result:
            phrase = result.split('"text" : "', 1)[-1].split('"', 1)[0]
            print(f"Você quer adicionar a frase '{phrase}'? (s/n)")
            answer = input()
            if answer.lower() == "s":
                recognized_phrase = phrase
                break
