package com.mkts.waac.services.impl;

import com.mkts.waac.Dao.DangerousPowDao;
import com.mkts.waac.Dto.DangerousPowDto;
import com.mkts.waac.mappers.DangerousPowMapper;
import com.mkts.waac.models.DangerousPow;
//import com.mkts.waac.mappers.DangerousPowMapper;
import com.mkts.waac.services.DangerousPowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DangerousPowServiceImpl implements DangerousPowService {

	private DangerousPowDao dangerousPowDao;

	private DangerousPowMapper dangerousPowMapper;

	@Autowired
	public DangerousPowServiceImpl(DangerousPowDao dangerousPowDao, DangerousPowMapper dangerousPowMapper) {
		this.dangerousPowDao = dangerousPowDao;
		this.dangerousPowMapper = dangerousPowMapper;
	}

	@Override
	public List<DangerousPowDto> getAll() {
		List<DangerousPow> allDangerousPow = dangerousPowDao.findAll();
		return dangerousPowMapper.convertToDtoList(allDangerousPow);
	}

	@Override
	public DangerousPowDto getOne(Integer id) {
		DangerousPow dangerousPow = dangerousPowDao.getOne(id);
		return dangerousPowMapper.convertToDto(dangerousPow);
	}

	@Override
	public void save(DangerousPowDto dangerousPowDto) {
		DangerousPow saveEntity = dangerousPowMapper.convertToEntity(dangerousPowDto);
		dangerousPowDao.save(saveEntity);
	}

	@Override
	public void delete(Integer id) {
		dangerousPowDao.deleteById(id);
	}
}
