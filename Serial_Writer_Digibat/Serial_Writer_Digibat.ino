#include <Time.h>

const byte HANDSHAKE[] = {85, 66, 68};
const byte HANDSHAKE_OK[] = {72, 79, 75};
const byte HEARTBEAT[] = {85, 78, 73};
const int led = 2;
const int pins[] = {A0, A1, A2, A3, A4, A5};
boolean ativo = false;
int quantidadePins = 4;

time_t tempo;

void setup() {
  tempo = now();
  
  pinMode(led, OUTPUT);
  Serial.begin(9600);
  Serial.write(HANDSHAKE, 3);
}

void loop() {
  if (ativo) {
    if (second(now()) - second(tempo) > 1) {
      Serial.write(HEARTBEAT, 3);
      delay(20);
      Serial.flush();
      tempo = now();
    }
    for (int i = 0; i < quantidadePins; i++) {
      int forca = analogRead(pins[i]);
    
      int len = 3;
      byte dados[len];
      mensagem(i, forca, dados);
      Serial.write(dados, len);
      delay(10);
    }
  } else if (Serial.available() == 3) {
    boolean comparador = true;
    for (int i = 0; i < 3; i++) {
      if (Serial.read() != HANDSHAKE_OK[i]) comparador = false;
    }
    if (comparador) {
      digitalWrite(led, HIGH);
      ativo = true;
    }
    delay(20);
  }
}

void mensagem(byte pin, int valor, byte bytes[]) {
    bytes[0] = pin;
    bytes[1] = valor & 0xFF;
    bytes[2] = (valor >> 8) & 0xFF;
}
