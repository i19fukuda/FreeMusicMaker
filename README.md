# FreeMusicMaker

授業の課題として製作中のJavaFxアプリです．誰でも簡単に作曲できるようなアプリになる予定です．

## 実行環境

JavaFxが実行できる環境で  
```javac Main.java```  
```java Main```  
をすることでアプリを起動できます．

## 操作方法

* 音符の入力について

    - 縦が音の高さ，横が時間軸になっています．上が低い音，下が高い音です．C5付近が聞きやすいと思います．
    - ダブルクリックで16分音符を入力することができます．コントロールキーを押しながら左クリックで音符の長さが伸びます．同じようにして右クリックをすると音符の長さが短くなります．最小で16分音符の長さまで小さくできます．ダブルクリックなど，クリックする回数が多いとその分長くすることができます．

* 再生の仕方について

   中央にあるplayボタンを押せば入力された音楽が再生されます．

* テンポの変更について

  中央にあるテキストフィールドにテンポを入力することで変更できます．いつでも変更できるようになっています．

* 楽器の変更について

  現時点では数値で楽器の変更ができるようになっています．入力フィールドにて0-127の数値を入力してください．
