package com.mkts.waac.services.impl;

import com.mkts.waac.Dao.ActivityKindDao;
import com.mkts.waac.Dto.ActivityKindDto;
import com.mkts.waac.mappers.ActivityKindMapper;
import com.mkts.waac.models.ActivityKind;
//import com.mkts.waac.mappers.ActivityKindMapper;
import com.mkts.waac.services.ActivityKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ActivityKindServiceImpl implements ActivityKindService {

	private ActivityKindDao activityKindDao;

	private ActivityKindMapper activityKindMapper;

	@Autowired
	public ActivityKindServiceImpl(ActivityKindDao activityKindDao, ActivityKindMapper activityKindMapper) {
		this.activityKindDao = activityKindDao;
		this.activityKindMapper = activityKindMapper;
	}


	@Override
	public List<ActivityKindDto> getAll() {
		List<ActivityKind> allActivityKinds = activityKindDao.findAll();
		return activityKindMapper.convertToDtoList(allActivityKinds);
	}

	@Override
	public ActivityKindDto getOne(Integer id) {
		ActivityKind activityKind = activityKindDao.getOne(id);
		return activityKindMapper.convertToDto(activityKind);
	}

	@Override
	public void save(ActivityKindDto activityKindDto) {
		ActivityKind saveEntity = activityKindMapper.convertToEntity(activityKindDto);
		activityKindDao.save(saveEntity);
	}

	@Override
	public void delete(Integer id) {
		activityKindDao.deleteById(id);
	}
}
