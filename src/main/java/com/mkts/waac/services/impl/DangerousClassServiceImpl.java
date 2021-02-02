package com.mkts.waac.services.impl;

import com.mkts.waac.Dao.DangerousClassDao;
import com.mkts.waac.Dto.DangerousClassDto;
import com.mkts.waac.mappers.DangerousClassMapper;
import com.mkts.waac.models.DangerousClass;
//import com.mkts.waac.mappers.DangerousClassMapper;
import com.mkts.waac.services.DangerousClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DangerousClassServiceImpl implements DangerousClassService {


	private DangerousClassDao dangerousClassDao;

	private DangerousClassMapper dangerousClassMapper;

	@Autowired
	public DangerousClassServiceImpl(DangerousClassDao dangerousClassDao, DangerousClassMapper dangerousClassMapper) {
		this.dangerousClassDao = dangerousClassDao;
		this.dangerousClassMapper = dangerousClassMapper;
	}

	@Override
	public List<DangerousClassDto> getAll() {
		List<DangerousClass> alldangerousClass = dangerousClassDao.findAll();
		return dangerousClassMapper.convertToDtoList(alldangerousClass);
	}

	@Override
	public DangerousClassDto getOne(Integer id) {
		DangerousClass dangerousClass = dangerousClassDao.getOne(id);
		return dangerousClassMapper.convertToDto(dangerousClass);
	}

	@Override
	public void save(DangerousClassDto dangerousClassDto) {
		DangerousClass saveEntity = dangerousClassMapper.convertToEntity(dangerousClassDto);
		dangerousClassDao.save(saveEntity);
	}

	@Override
	public void delete(Integer id) {
		dangerousClassDao.deleteById(id);
	}
}
