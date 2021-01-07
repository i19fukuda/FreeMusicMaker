# FreeMusicMaker

授業の課題として製作中のJavaFxアプリです．誰でも簡単に作曲できるようなアプリになる予定です．

## 実行環境

JavaFxが実行できる環境で  
```javac Main.java```  
```java Main```  
をすることでアプリを起動できます．

## 操作方法

* 音符の入力について
  * 縦が音の高さ，横が時間軸になっています．上が高い音，下が低い音です．C4付近が聞きやすいと思います．
  * 入力方法はそれぞれ以下のようになっています．
    * ダブルクリック : ノートの入力
    * shift + 左クリック : 音を長く
    * shft + 右クリック : 音を短く
    * ctrl + 左クリック : 複数選択
    * ctrl + 右クリック : ノートの削除

* 再生の仕方について

   左上のplayボタンにて再生できます．

* テンポの変更について

  左上のテキストフィールドにて数値を入力してください．

* トラックについて

  トラックという単位での曲作りができます．トラックでは楽器の種類が選択できます．トラックの追加は上のadd Lineボタンから，削除はremove Lineボタンからできます．

* 音のMixについて

  一つのトラックについて，楽器の音を調整することができます．これは元の音符にオクターブの音を重ねることによって実現します．Mixボタンを押すことで調整することができます．デフォルトではすべて0に設定してあります．

* 楽器の変更について

  初期状態ではinstとなっています．そこをクリックして楽器を選択してください．デフォルトでピアノが選択されています．

* プロジェクトの保存について

  現時点では簡易的なプロジェクトの保存ができるようになっています．メニューバーのファイルからsaveを選択してください．

* プロジェクトファイルの読み込みについて

  現時点では簡易的なプロジェクトの読み込みができるようになっています．各トラックについて楽器の情報や音符の情報が保持されます．複数のプロジェクトの同時保存は今後対応予定です．
