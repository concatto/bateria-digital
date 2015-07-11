#include <Time.h>

const byte COMM = 0b0000;
const byte PIN_OFF = 0b0001;
const byte PIN_ON = 0b0010;
const byte BEAT = 0b0001;

const byte KEEPALIVE = 0b0001;
const byte HANDSHAKE = 0b0010;

const int led = 2;
const int pins[] = {A0, A1, A2, A3, A4, A5};
int estado = 0b0000000000000000;
boolean ativo = false;

time_t tempo;

void setup() {
  tempo = now();
  
  pinMode(led, OUTPUT);
  Serial.begin(9600);
  Serial.write(codificar(COMM, HANDSHAKE));
}

void loop() {
  if (ativo) {
    if (second(now()) - second(tempo) > 1) {
      Serial.write(codificar(COMM, KEEPALIVE));
      delay(10);
      tempo = now();
    }
    for (int i = 0; i < 0xF; i++) {
      int forca = analogRead(pins[i]);
      int len = 3;
      byte dados[len];
      mensagem(i, forca, dados);
      Serial.write(dados, len);
      delay(5);
    }
  }
  
  if (Serial.available() > 0) {
    boolean comparador = true;
    for (int i = 0; i < 3; i++) {
      if (Serial.read() != HANDSHAKE ) comparador = false;
    }
    if (comparador) {
      digitalWrite(led, HIGH);
      ativo = true;
    }
    delay(20);
  }
}

void enviarBatidas() {
  for (int i = 0; i < 0xF; i++) {
    if (getEstado(i)) {
      int forca = analogRead(pins[i]);
      int len = 3;
      byte dados[len];
      dados[0] = codificar(BEAT, i);
      mensagem(forca, dados);
      Serial.write(dados, len);
      delay(5);
    }
  }
}

void separar(int valor, byte destino[]) {
    destino[1] = valor & 0xFF;
    destino[2] = (valor >> 8) & 0xFF;
}

byte codificar(byte comando, byte dados) {
  return (comando << 4) | dados;
}

int getEstado(int indice) {
  return (estado >> indice) & 1;
}

void setEstado(int novoEstado, int indice) {
  estado = estado ^ ((-novoEstado ^ estado) & (1 << indice));
}

