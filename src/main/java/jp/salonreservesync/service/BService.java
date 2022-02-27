package jp.salonreservesync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.salonreservesync.dao.ReservesDao;
import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.scraping.a.*;
import jp.salonreservesync.scraping.b.*;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 予約サイトBを操作するサービス
 */
@Service
public class BService
{
  @Autowired
  private ReservesDao reservesDao;

  /**
   * ダミーの予約を登録
   * @param order orderDto
   */
  public void dummyReserve(OrderDto order)
  {
    // 予約サイトAから、予約の詳細を取得
    Runner runner =
      Runner.of(order)
          .add(
            new ALogin(),
            new AGetDetail()
          )
          .execute();

    reservesDao.create(order);

    runner.add(
            new BLogin(),
            new BAdjustYearMonth(),
            new BAdjustDay(),
            new BAdjustRowCellReserve(),
            new BDummyReserve()
          )
          .execute()
          .closeWeb();
  }

  /**
   * ダミーの予約をキャンセル
   * @param order orderDto
   */
  public void dummyCancel(OrderDto order)
  {
    OrderDto cancel = reservesDao.read(order);

    Runner.of(cancel)
          .add(
            new BLogin(),
            new BAdjustYearMonth(),
            new BAdjustDay(),
            new BAdjustRowCellCancel(),
            new BDummyCancel()
          )
          .execute()
          .closeWeb();
  }

  /**
   * ダミーの予約を変更
   * @param order orderDto
   */
  public void dummyChange(OrderDto order)
  {
    OrderDto cancel = reservesDao.read(order);
    reservesDao.create(order);

    Runner.of(cancel)
          .add(
            new BLogin(),
            new BAdjustYearMonth(),
            new BAdjustDay(),
            new BAdjustRowCellCancel(),
            new BDummyCancel()
          )
          .execute()
          .add(
            new BAdjustYearMonth(),
            new BAdjustDay(),
            new BAdjustRowCellReserve(),
            new BDummyReserve()
          )
          .execute()
          .closeWeb();
  }
}
