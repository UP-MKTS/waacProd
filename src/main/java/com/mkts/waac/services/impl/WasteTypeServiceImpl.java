package com.mkts.waac.services.impl;

import com.mkts.waac.Dao.WasteTypeDao;
import com.mkts.waac.Dto.WasteTypeDto;
import com.mkts.waac.models.WasteType;
import com.mkts.waac.mappers.WasteTypeMapper;
import com.mkts.waac.services.WasteTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WasteTypeServiceImpl implements WasteTypeService {


	private WasteTypeDao wasteTypeDao;

	private WasteTypeMapper wasteTypeMapper;

	@Autowired
	public WasteTypeServiceImpl(WasteTypeDao wasteTypeDao, WasteTypeMapper wasteTypeMapper) {
		this.wasteTypeDao = wasteTypeDao;
		this.wasteTypeMapper = wasteTypeMapper;
	}

	@Override
	public List<WasteTypeDto> getAll() {
		List<WasteType> allWasteTypes = wasteTypeDao.findAll();
		return wasteTypeMapper.convertToDtoList(allWasteTypes);
	}

	@Override
	public List<WasteTypeDto> getAllByListId(List<Integer> indexes) {
		List<WasteType> wasteTypes = wasteTypeDao.findAllById(indexes);
		return wasteTypeMapper.convertToDtoList(wasteTypes);
	}

	@Override
	public WasteTypeDto getOne(Integer id) {
		WasteType wasteType = wasteTypeDao.getOne(id);
		return wasteTypeMapper.convertToDto(wasteType);
	}

	@Override
	public void save(WasteTypeDto wasteTypeDto) {
		WasteType saveEntity = wasteTypeMapper.convertToEntity(wasteTypeDto);
		wasteTypeDao.save(saveEntity);
	}

	@Override
	public void delete(Integer id) {
		wasteTypeDao.deleteById(id);
	}

	@Override
	public List<WasteTypeDto> getFromAccompPassp(Integer departmentId) {
		return null;
	}
	//	@Override
//	public List<WasteTypeDto> getFromAccompPassp(Integer departmentId) {
//		List<WasteType> wasteType = wasteTypeDao.findWasteTypeByDepartmentId(departmentId);
//		return wasteTypeMapper.convertToDtoList(wasteType);
//	}
}
