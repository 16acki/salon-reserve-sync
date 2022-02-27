package jp.salonreservesync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.salonreservesync.dao.ReservesDao;
import jp.salonreservesync.dto.OrderDto;
import jp.salonreservesync.scraping.a.*;
import jp.salonreservesync.scraping.common.Runner;

/**
 * 予約サイトAを操作するサービス
 */
@Service
public class AService
{
  @Autowired
  private ReservesDao reservesDao;

  /**
   * ダミーの予約を登録
   * @param order orderDto
   */
  public void dummyReserve(OrderDto order)
  {
    reservesDao.create(order);

    Runner.of(order)
          .add(
            new ALogin(),
            new AAdjustYearMonth(),
            new AAdjustDay(),
            new AAdjustRowCellReserve(),
            new ADummyReserve()
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
            new ALogin(),
            new AAdjustYearMonth(),
            new AAdjustDay(),
            new AAdjustRowCellCancel(),
            new ADummyCancel()
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
            new ALogin(),
            new AAdjustYearMonth(),
            new AAdjustDay(),
            new AAdjustRowCellCancel(),
            new ADummyCancel()
          )
          .execute()
          .changeOrder(order)
          .add(
            new AAdjustYearMonth(),
            new AAdjustDay(),
            new AAdjustRowCellReserve(),
            new ADummyReserve()
          )
          .execute()
          .closeWeb();
  }
}
