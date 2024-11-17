# 楽天ランキング情報自動ツイートBOT

楽天市場のランキング情報を自動ツイートする。

---

## **環境変数の設定方法**

### 1. **環境変数設定ファイル**

このプロジェクトでは、必要な環境変数を以下のファイルに設定する：

- `~/.bashrc.d/custom_env_vars.sh`

このファイルに環境変数を記述し、`~/.bashrc` から読み込むようにする。

### 2. **環境変数設定ファイルの確認方法**

環境変数設定ファイル（`~/.bashrc.d/custom_env_vars.sh`）の中身を確認するには、以下のコマンドを実行する。

```bash
# ファイルの中身を確認
cat ~/.bashrc.d/custom_env_vars.sh
```
### 3. **環境変数設定方法**
```bash
# nanoエディタで環境変数設定ファイルを開く
nano ~/.bashrc.d/custom_env_vars.sh
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
