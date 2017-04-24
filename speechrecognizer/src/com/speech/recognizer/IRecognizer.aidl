// IRecognizer.aidl
package com.speech.recognizer;

import com.speech.recognizer.IRecognizerListener;
// Declare any non-default types here with import statements

interface IRecognizer {
      void start();
      void stop();
      void shutdown();
      void stopSound();
      int getState();
      void registerListener(IBinder token, IRecognizerListener listener);
      void unregisterListener(IBinder token);
}
