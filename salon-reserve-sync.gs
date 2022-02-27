/**
 * 【エンドポイント】
 */
// TODO
const ENDPOINT = 'https://xxx.xxx.xxx.xxx:8080';

/**
 * 【ログイン関数】
 * 認証用の JWT、GMail の検索文字列を取得します。
 */
function login()
{
  var payload = {
    'userId': Session.getActiveUser().getUserLoginId()
  };

  var options = {
    'method': 'post',
    'contentType': 'application/json',
    'payload': JSON.stringify(payload)
  };

  var response = UrlFetchApp.fetchAll(ENDPOINT + '/login', options);

  if (200 == response.getResponseCode())
  {
    var json = JSON.parse(response.getContentText());
    var prop = PropertiesService.getScriptProperties();
    prop.setProperty('x-auth-token', json['token']);
    prop.setProperty('gmail_search_string', json['gmail_search_string']);
    console.log("成功");
  }
  else
  {
    console.log("エラー");
  }
}

/**
 * 【メイン関数】
 */
function main()
{
  var prop = PropertiesService.getScriptProperties();
  var gmail_search_string = prop.getProperty('gmail_search_string');
  var threads = GmailApp.search(gmail_search_string, 0, 10);

  threads.forEach(function(thread)
  {
    var msgs = thread.getMessages();

    msgs.forEach(function(msg)
    {
      // ★が付いていたら処理済みなので、飛ばす
      if (msg.isStarred())
      {
        return;
      }

      var headers = {
        'X-AUTH-TOKEN': prop.getProperty('x-auth-token')
      };

      var subject = msg.getSubject(); // 件名
      var body = msg.getPlainBody();  // 本文

      var payload = {
        'subject': subject,
        'body'   : body,
      };

      var options = {
        'method' : 'post',
        'contentType': 'application/json',
        'headers': headers,
        'payload': JSON.stringify(payload)
      };

      var response = UrlFetchApp.fetchAll(ENDPOINT + '/scraping', options);
      var code = response.getResponseCode();

      if (200 == code)
      {
        // 処理成功したら★を付ける
        msg.star();
      }
      else
      {
        // 同期失敗時にメール送信
        var gmail = Session.getActiveUser().getUserLoginId();

        var tmp = "以下のご予約に関して、同期が失敗しました。\n";
        tmp += "お手数ですが、手入力でスケジュールを入力してください。\n";
        tmp += "===========================================================\n";
        tmp += body;
        
        GmailApp.sendEmail(gmail, "【同期失敗報告】", tmp);
      }
    });
  });
}
