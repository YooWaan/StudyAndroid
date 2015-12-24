+++
date = "2015-12-23T17:00:00+09:00"
draft = true
title = "Development"

+++


## 環境の整理


### セットアップ

* SDK,Java,Gradle or and をセットアップ (Android Studioを使用していないので、 Studioを使用するといろいろ不要かも...)
* エミュレータorデバイスを用意する

### サイクル


### Emulator

<pre>
## Android virtual device (avd) の確認
$ android list avd



## エミュレータで使用できるものを確認 (id, skinをメモる)
$ android list targets
Available Android targets:
----------
id: 1 or "android-8"
     Name: Android 2.2
     Type: Platform
     API level: 8
     Revision: 3
     Skins: HVGA, QVGA, WQVGA400, WQVGA432, WVGA800 (default), WVGA854
 Tag/ABIs : default/armeabi
----------
id: 2 or "android-10"
     Name: Android 2.3.3
     Type: Platform
     API level: 10
     Revision: 2
     Skins: HVGA, QVGA, WQVGA400, WQVGA432, WVGA800 (default), WVGA854
 Tag/ABIs : default/armeabi


## avd を作成 (API レベル23 のもので default のskinを選択)
$ android create avd -n AVD01 -t 11 -s WVGA800

</pre>

### Project


