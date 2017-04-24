// IRecognizer.aidl
package com.speech.recognizer;
import android.os.Bundle;
// Declare any non-default types here with import statements

interface IRecognizerListener {
      void onResults(int code);
      void onError(int err);
}
