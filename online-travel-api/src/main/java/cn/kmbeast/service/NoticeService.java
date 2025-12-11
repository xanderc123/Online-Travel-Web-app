package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.HotelQueryDto;
import cn.kmbeast.pojo.dto.query.extend.NoticeQueryDto;
import cn.kmbeast.pojo.entity.Hotel;
import cn.kmbeast.pojo.entity.Notice;
import cn.kmbeast.pojo.vo.HotelVO;

import java.util.List;

public interface NoticeService {

    Result<Void> save(Notice notice);

    Result<Void> update(Notice notice);

    Result<Void> batchDelete(List<Integer> ids);

    Result<List<Notice>> query(NoticeQueryDto dto);

}
