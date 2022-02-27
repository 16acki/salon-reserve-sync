package jp.salonreservesync.dao;

import java.io.File;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * シークレットDAO
 */
@Repository
public class SecretDao
{
  private static final String STORE = "secret.xml";

  /**
   * 保存
   * @param userId
   * @param secretKey
   */
  public void create(String userId, String secretKey)
  {
    try
    {
      // XML ファイル
      File file = Paths.get(STORE).toFile();

      // ドキュメント取得
      Document document =
        DocumentBuilderFactory.newInstance()
                              .newDocumentBuilder()
                              .parse(file);

      // key タグ取得
      Element key = (Element)document.getElementsByTagName("key").item(0);
      // 属性設定
      key.setAttribute("userId", userId);
      key.setAttribute("secretKey", secretKey);

      Transformer transformer =
        TransformerFactory.newInstance()
                          .newTransformer();

      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
      // transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      // transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "2");

      transformer.transform(
        new DOMSource(document),
        new StreamResult(file)
      );
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * 取得
   * @param userId
   * @return secretKey
   */
  public String read(String userId)
  {
    try
    {
      // XML ファイル
      File file = Paths.get(STORE).toFile();

      // ドキュメント取得
      Document document =
        DocumentBuilderFactory.newInstance()
                              .newDocumentBuilder()
                              .parse(file);

      // key タグ取得
      Element key = (Element)document.getElementsByTagName("key").item(0);

      return key.getAttribute("secretKey");
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
