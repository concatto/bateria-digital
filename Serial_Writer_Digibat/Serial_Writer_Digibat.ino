#include <Time.h>

const byte HANDSHAKE[] = {85, 66, 68};
const byte HANDSHAKE_OK[] = {72, 79, 75};
const byte HEARTBEAT[] = {85, 78, 73};
const int pins[] = {A0, A1};
boolean ativo = false;

time_t tempo;

void setup() {
  tempo = now();
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
    for (int i = 0; i < 2; i++) {
      int forca = analogRead(pins[i]);
    
      int len = 3;
      byte data[len];
      message(i, forca, data);
      Serial.write(data, len);
      delay(10);
    }
  } else if (Serial.available() == 3) {
    boolean comparador = true;
    for (int i = 0; i < 3; i++) {
      if (Serial.read() != HANDSHAKE_OK[i]) comparador = false;
    }
    if (comparador) {
      digitalWrite(12, HIGH);
      ativo = true;
    }
    delay(20);
  }
}

void message(byte pin, int value, byte bytes[]) {
    bytes[0] = pin;
    bytes[1] = value & 0xFF;
    bytes[2] = (value >> 8) & 0xFF;
}
