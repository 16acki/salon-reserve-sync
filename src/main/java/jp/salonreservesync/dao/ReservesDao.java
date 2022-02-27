package jp.salonreservesync.dao;

import java.io.File;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.exception.BusinessException;
import lombok.Synchronized;

/**
 * 予約情報DAO
 */
@Repository
public class ReservesDao
{
  private static final String STORE = "reserves.xml";

  /**
   * レコード生成
   * @param order OrderDto
   */
  @Synchronized
  public void create(OrderDto order)
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

      // ルートを取得
      Element root = document.getDocumentElement();

      // レコード生成
      Element reserve = document.createElement("reserve");
      reserve.setAttribute("site", order.getSite().getValue());
      reserve.setAttribute("operation", order.getOperation().toString());
      reserve.setAttribute("reserveId", order.getReserveId());
      reserve.setAttribute("year", order.getYear());
      reserve.setAttribute("month", order.getMonth());
      reserve.setAttribute("day", order.getDay());
      reserve.setAttribute("startHour", order.getStartHour());
      reserve.setAttribute("startMinute", order.getStartMinute());
      reserve.setAttribute("runTime", String.valueOf(order.getRunTime()));
      reserve.setAttribute("endHour", order.getEndHour());
      reserve.setAttribute("endMinute", order.getEndMinute());
      reserve.setAttribute("staff", order.getStaff());

      // ルートにレコードを追加
      root.appendChild(reserve);

      // 保存
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
   * サイト + 予約IDの直近レコードを取得する
   * @param order
   * @return
   */
  public OrderDto read(OrderDto order)
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

      // 子ノードすべて取得
      NodeList items = document.getElementsByTagName("reserve");

      int L = items.getLength() - 1;
      for (int i = L; i >= 0; i--)
      {
        Node node = items.item(i);

        if (node.getNodeType() != Node.ELEMENT_NODE)
        {
          continue;
        }

        Element item = (Element)node;

        // 予約IDの一致をチェックする
        if (!item.getAttribute("reserveId").equals(order.getReserveId()))
        {
          continue;
        }

        // サイトの一致をチェックする
        if (!item.getAttribute("site").equals(order.getSite().getValue()))
        {
          continue;
        }

        return new OrderDto(item);
      }

      throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR,
                                  "レコードがありません。site=" + order.getSite().getValue() + ", reserveId=" + order.getReserveId());
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
