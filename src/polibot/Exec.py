import speech_recognition as sr
import pyautogui

def ouvir():
    r = sr.Recognizer()

    while True:
        with sr.Microphone() as source:
            print("Ouvindo...")
            audio = r.listen(source)
        try:
            texto = r.recognize_google(audio, language='pt-BR')
            print(f"Você disse: {texto}")
            if 'passar slide' in texto:
                pyautogui.press('right')
            elif 'voltar slide' in texto:
                pyautogui.press('left')
        except sr.UnknownValueError:
            print("Não entendi o que você disse.")
        except sr.RequestError as e:
            print(f"Não foi possível completar a requisição: {e}")

ouvir()
