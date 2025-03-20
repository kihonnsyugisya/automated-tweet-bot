# 楽天ランキング情報自動ツイートBOT

楽天市場のランキング情報を自動ツイートする。

アカウント
https://x.com/RakutenOtaku

---

## **環境変数の設定方法**

### 1. **環境変数設定ファイル**

このプロジェクトでは、必要な環境変数を以下のファイルに設定する：

- `~/.bashrc.d/custom_env_vars.sh`

このファイルに環境変数を記述し、`~/.bashrc` から読み込むようにする。

### 2. **環境変数設定ファイルの確認方法**

環境変数設定ファイル（`/etc/default/automated-affiliate-bot`）の中身を確認するには、以下のコマンドを実行する。

```bash
# ファイルの中身を確認
cat /etc/default/automated-affiliate-bot
```
### 3. **環境変数設定方法**
```bash
# nanoエディタで環境変数設定ファイルを開く
nano /etc/default/automated-affiliate-bot
```

反映コマンド
```bash
# ~/.bashrc を再読み込みして環境変数を反映
source ~/.bashrc
```

## 使用しているJavaのバージョンとライブラリ

### Javaバージョン
- 使用しているJavaバージョン: **Java 21**
- Gradleの `toolchain` を使用してJava 21を指定。

### ライブラリ

#### Spring Boot
- **Spring Boot バージョン**: **3.3.4**
- 以下のSpring Bootスタートパックを使用:
  - `spring-boot-starter-batch`: バッチ処理用
  - `spring-boot-starter-mail`: メール送信機能
  - `spring-boot-starter-thymeleaf`: HTMLテンプレートエンジンとしてThymeleaf
  - `spring-boot-starter-web`: RESTful API/Webアプリケーション

#### MyBatis
- **MyBatis バージョン**: **3.0.3**
- データベース操作用ライブラリとして `mybatis-spring-boot-starter` を使用。

#### Twitter API
- **Twittered ライブラリ**: **2.23**
- Twitter APIとの連携に使用。

#### PostgreSQL
- **PostgreSQL JDBC ドライバー**
  - `postgresql`: PostgreSQLデータベースと接続するためのJDBCドライバー

#### その他
- **Lombok**: ボイラープレートコードを削減するために使用。
  - `lombok`: コンパイル時にコード生成をサポート。


#### テスト
- **JUnit 5**: テストフレームワークとして使用。
  - `spring-boot-starter-test`: Spring Bootプロジェクトのテストサポート。
  - `mockito-core`: モックライブラリとしてMockitoを使用。
  
  
  
  # デプロイ手順と設定

## JDKのインストール

### Amazon Linux 2023でJDK 21のインストール

1. **Amazon Linux 2023でJDK 21のインストール**
   - `dnf` を使って `amazon-corretto` パッケージをインストール。
   
   ```bash
   sudo dnf install java-21-amazon-corretto-devel
   ```

2. **JDKのバージョン確認**
   - インストール後、`java -version` でバージョンを確認。

   ```bash
   java -version
   ```

## アプリケーションのデプロイ


### `application.properties` の設定変更

   - 本番環境にデプロイする前に、`application.properties` で `prod` プロファイルを有効にする
   
   ```properties
   # 共通環境用設定
   # デプロイするときだけ以下のコメントアウトを外すこと
   #spring.profiles.active=prod
   ```

   - 上記のコメントアウト部分を外すことで、`prod` プロファイルが適用され、アプリケーションが本番環境向けに設定されます。
   
### JARファイルのビルド

## ビルドコマンド

1. **JARファイルのビルド**
   - Gradleを使用してJARファイルをビルドします。
   
   以下のコマンドを実行して、JARファイルを作成。

   ```bash
   ./gradlew build
   ```

2. **ビルド後のJARファイル**
   - ビルドが成功すると、以下の場所にJARファイルが生成されます。

   ```plaintext
   build/libs/automated-affiliate-bot-0.0.1-SNAPSHOT.jar
   ```

### JARファイルの配置

- **成果物の場所**
  - ローカルでビルドしたJARファイルは以下のディレクトリに保存されている。
  
  ```plaintext
  D:\JavaProjects\automated_tweet_bot\build\libs\automated-affiliate-bot-0.0.1-SNAPSHOT.jar
  ```
  
- **SCPコマンドを使ってEC2に転送**
  - 以下のコマンドでEC2にファイルを転送。

  ```bash
  scp -i "pemのパス" D:/JavaProjects/automated_tweet_bot/build/libs/automated-affiliate-bot-0.0.1-SNAPSHOT.jar ec2-user@<EC2_IP>:/home/ec2-user/
  ```

  - ここで、`<EC2_IP>` は実際のEC2インスタンスのIPアドレス。

### デプロイ先の確認

- EC2のインスタンスにログインして、JARファイルが正しく配置されているか確認。

  ```bash
  ls -l /home/ec2-user/
  ```

## PostgreSQLの設定
 **PostgreSQLのサービス有効化**
   - PostgreSQLの自動起動を有効にする。

   ```bash
   sudo systemctl enable postgresql
   sudo systemctl start postgresql
   ```

## アプリケーションの自動起動設定

### Systemdでアプリケーションを自動起動させる設定

1. **`automated-affiliate-bot`のSystemdサービス設定**
   - 以下の内容で、EC2インスタンス起動時にアプリケーションが自動起動するように設定。

   ```bash
   sudo vi /etc/systemd/system/automated-affiliate-bot.service
   ```

2. **サービスの有効化と起動**
   - 作成したサービスファイルを有効化し、EC2インスタンス起動時に自動的にアプリケーションを起動するように設定。

   ```bash
   sudo systemctl daemon-reload
   sudo systemctl enable automated-affiliate-bot
   sudo systemctl start automated-affiliate-bot
   ```

3. **自動起動の確認**
   - 以下のコマンドでサービスが起動しているか確認。

   ```bash
   sudo systemctl status automated-affiliate-bot
   ```

  
