package com.mkts.waac.services.impl;

import com.mkts.waac.Dao.*;
import com.mkts.waac.Dto.*;
import com.mkts.waac.mappers.*;
import com.mkts.waac.models.*;
import com.mkts.waac.services.*;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.cglib.core.CollectionUtils.filter;

@Service
@Transactional
public class AccompPasspServiceImpl implements AccompPasspService {

	private AccompPasspDao accompPasspDao;

	private AccompPasspWasteService accompPasspWasteService;

	private WasteTypeService wasteTypeService;

	private AccompPasspMapper accompPasspMapper;

	@Autowired
	private AccompPasspWasteDao accompPasspWasteDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private Pod9OwnWasteService pod9OwnWasteService;

	@Autowired
	private ContractDao contractDao;

	@Autowired
	private GoalMapper goalMapper;

	@Autowired
	private WasteTypeMapper wasteTypeMapper;

	@Autowired
	private DepartmentMapper departmentMapper;

	@Autowired
	private AccompPasspDepartmentMapper accompPasspDepartmentMapp;

	@Autowired
	private AccompPasspDepartmentService accompPasspDepartmentService;

	@Autowired
	private WasteTypeDao wasteTypeDao;

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private GoalDao goalDao;

	@Autowired
	private AccompPasspWasteMapper accompPasspWasteMapper;

	@Autowired
	public AccompPasspServiceImpl(AccompPasspDao accompPasspDao, AccompPasspWasteService accompPasspWasteService, WasteTypeService wasteTypeService, AccompPasspMapper accompPasspMapper) {
		this.accompPasspDao = accompPasspDao;
		this.accompPasspWasteService = accompPasspWasteService;
		this.wasteTypeService = wasteTypeService;
		this.accompPasspMapper = accompPasspMapper;

	}


	@Override
	public List<AccompPasspDto> getAll() {
		List<AccompPassp> all = accompPasspDao.findAll();
		List<AccompPasspDto> accompPasspDtos = accompPasspMapper.convertToDtoList(all);
		return  accompPasspDtos;
	}

	@Override
	public AccompPasspDto getOne(Integer accompPasspId) {
		AccompPassp accompPassp = accompPasspDao.findById(accompPasspId).get();
		return  accompPasspMapper.convertToDto(accompPassp);
	}

	@Override
	public void delete(Integer id) {
		AccompPassp accompPassp = accompPasspDao.findById(id).get();
		for (AccompPasspWaste accompPasspWaste:accompPassp.getAccompPasspWastes())
		{
			pod9OwnWasteService.deleteByAccopmPasspWasteId(accompPasspWaste.getId());
			accompPasspWasteService.delete(accompPasspWaste.getId());
		}
		accompPasspDepartmentService.delete(id);
		accompPasspDao.deleteById(id);
	}

	//	@Override
//	public List<AccompPasspDto> getAll() {
//		List<AccompPassp> allAccompPassps = accompPasspDao.findAll(Sort.by("number").descending());
//		StringBuilder wasteCodes = new StringBuilder();
//		Map<Integer, String> wasteCodeMap = new HashMap<>();
//		for (AccompPassp accompPassp: allAccompPassps) {
//			wasteCodes.setLength(0);
//			if (accompPassp.getAccompPasspWastes().size() > 0) {
//				for (AccompPasspWaste accompPasspWaste : accompPassp.getAccompPasspWastes()) {
//					wasteCodes.append(wasteTypeService.getOne(accompPasspWaste.getWasteTypeId()).getCode()).append(", ");
//				}
//				int strLength = wasteCodes.length();
//				wasteCodes.delete(strLength - 2, strLength);
//				wasteCodeMap.put(accompPassp.getId(), wasteCodes.toString());
//			}
//		}
//		List<AccompPasspDto> accompPasspDtos = accompPasspMapper.convertToDtoList(allAccompPassps);
//
//		for (AccompPasspDto accompPasspDto: accompPasspDtos) {
//			if (wasteCodeMap.containsKey(accompPasspDto.getId())) {
//				accompPasspDto.setWasteCode(wasteCodeMap.get(accompPasspDto.getId()));
//			}
//		}
//		return accompPasspDtos;
//	}

	@Override
	public List<AccompPasspJournalDto> getAllByYearAndMonth(String year, String month) {
		List<AccompPassp> allAccompPassps = accompPasspDao.getAllByYearAndMonth(year, month);
		return accompPasspMapper.convertToJournalsDtoList(allAccompPassps);
	}
//
//	@Override
//	public AccompPasspDto getOne(Integer id) {
//		AccompPassp accompPassp = accompPasspDao.getOne(id);
//		return accompPasspMapper.convertToDto(accompPassp);
//	}
//
	@Override
	public void save(AccompPasspDto accompPasspDto) {
		AccompPassp saveEntity = accompPasspMapper.convertToEntity(accompPasspDto);
		accompPasspDao.save(saveEntity);
		AccompPassp lastAccompPassp = accompPasspDao.findFirstByNumber(accompPasspDto.getNumber());
		accompPasspWasteService.saveAll(accompPasspDto, lastAccompPassp);
	}

	@Override
	public void saveUpdete(AccompPasspSaveDto accompPasspSaveDto) {
		AccompPasspDto temp = accompPasspMapper.convertToDto(accompPasspDao.findById(accompPasspSaveDto.getId()).get());
		accompPasspSaveDto.getWasteTypeIdList();
		temp.setAccompPasspDate(accompPasspSaveDto.getAccompPasspDate());
		temp.setBoxing(accompPasspSaveDto.getBoxing());
		temp.setCarModel(accompPasspSaveDto.getCarModel());
		temp.setCarNumber(accompPasspSaveDto.getCarNumber());
		Organization carrierOrg = organizationDao.findById(accompPasspSaveDto.getCarrierOrganizationId()).get();
		temp.setCarrierOrganizationId(carrierOrg.getId());
		temp.setCarrierOrganizationAddress(carrierOrg.getAddress());
		temp.setCarrierOrganizationName(carrierOrg.getName());
		Contract contract = contractDao.findById(accompPasspSaveDto.getContractId()).get();
		temp.setContractDate(contract.getContractDate().toString());
		temp.setContractId(contract.getId());
		temp.setContractNumber(contract.getNumber());
		temp.setDriverFio(accompPasspSaveDto.getDriverFio());
		temp.setNumber(accompPasspSaveDto.getNumber());
		temp.setTransportationDate(accompPasspSaveDto.getTransportationDate());
		temp.setRecipientOrganizationAddress(contract.getOrganization().getAddress());
		temp.setRecipientOrganizationName(contract.getOrganization().getName());
		temp.setWasteDestination(contract.getOrganization().getWasteDestination());
		temp.setAddress(accompPasspSaveDto.getAddress());
		List<Integer> allDepatrments = new ArrayList<>();
		for(Integer i: accompPasspSaveDto.getDepartmentList())
		{
			allDepatrments.add(i);
		}

		for (int i = 0; i<temp.getDepartmentDtos().stream().count();i++){
			allDepatrments.add(temp.getDepartmentDtos().get(i).getDepartment().getId());
		}
		List<Integer> curDep = new ArrayList<>();
		for (AccompPasspDepartmentDto dto: temp.getDepartmentDtos()){
			curDep.add(dto.getDepartment().getId());
		}

		List<Integer> newList = allDepatrments.stream().distinct().collect(Collectors.toList());
		for (int i = 0; i<newList.stream().count();i++)
		{
			if(!accompPasspSaveDto.getDepartmentList().contains(newList.get(i))){
				accompPasspDepartmentService.delete(temp.getId(),newList.get(i));
				accompPasspDepartmentService.save();
			}
		}

		for (int i = 0; i<newList.stream().count();i++){
			if(!curDep.contains(newList.get(i))){
				AccompPasspDepartmentDto accompPasspDepartmentDto = new AccompPasspDepartmentDto();
				accompPasspDepartmentDto.setDepartment(departmentMapper.convertToDto(departmentDao.findById(newList.get(i)).get()));
				AccompPasspDepartment newWaste = accompPasspDepartmentMapp.convertToEntity(accompPasspDepartmentDto);
				newWaste.setAccompPassps(accompPasspMapper.convertToEntity(temp));
				accompPasspDepartmentService.save(newWaste);
			}
		}

		List<Integer> wastesId = new ArrayList<>();

		for (AccompPasspWasteDto waste:temp.getWasteTypeIdList()){
			wastesId.add(waste.getWasteTypeId().getId());
		}


		List<Integer> allWaste = new ArrayList<>();
		for(Integer i:accompPasspSaveDto.getWasteTypeIdList())
		{
			allWaste.add(i);
		}
		for (int i = 0; i<temp.getWasteTypeIdList().stream().count();i++){
			allWaste.add(temp.getWasteTypeIdList().get(i).getWasteTypeId().getId());
		}
		List<Integer> newListWaste = allWaste.stream().distinct().collect(Collectors.toList());
		for (int i = 0; i<newListWaste.stream().count();i++)
		{
			if(!accompPasspSaveDto.getWasteTypeIdList().contains(newListWaste.get(i))){
				pod9OwnWasteService.deleteByAccopmPasspWasteId(accompPasspWasteService.findDyAccompPasspIdAndWasteId(temp.getId(),newListWaste.get(i)).getId());
				accompPasspWasteService.deleteByAccompPasspIdAndWasteId(temp.getId(),newListWaste.get(i));
			}
		}

		for (int i = 0; i<newListWaste.stream().count();i++){
			if(!wastesId.contains(newListWaste.get(i))){
				AccompPasspWasteDto dto = new AccompPasspWasteDto();
				dto.setGoal(goalMapper.convertToDto(goalDao.findFirst1ByOrderById()));
				dto.setWasteWeight(0.0);
				dto.setWasteTypeId(wasteTypeMapper.convertToDto(wasteTypeDao.findById(newListWaste.get(i)).get()));
				AccompPasspWaste newWaste = accompPasspWasteMapper.convertToEntity(dto);
				newWaste.setAccompPassps(accompPasspMapper.convertToEntity(temp));
				accompPasspWasteService.save(newWaste);
				pod9OwnWasteService.save(newWaste);
			}
		}
		AccompPassp ap = accompPasspMapper.convertToEntity(temp);
		ap = accompPasspDao.save(ap);

//		accompPasspDepartmentService.deleteAll(accompPasspSaveDto.getId());

//		for (int waste = 0; waste<accompPasspSaveDto.getDepartmentList().stream().count();waste++){
//			AccompPasspDepartmentDto accompPasspDepartmentDto = new AccompPasspDepartmentDto();
//			accompPasspDepartmentDto.setDepartment(departmentMapper.convertToDto(departmentDao.findById(accompPasspSaveDto.getDepartmentList().get(waste)).get()));
//			AccompPasspDepartment newWaste = accompPasspDepartmentMapp.convertToEntity(accompPasspDepartmentDto);
//			newWaste.setAccompPassps(ap);
//			accompPasspDepartmentService.save(newWaste);
//		}
//		accompPasspWasteService.deleteAll(accompPasspSaveDto.getId());
//		for (int waste = 0; waste<accompPasspSaveDto.getWasteTypeIdList().stream().count();waste++){
//			AccompPasspWasteDto dto = new AccompPasspWasteDto();
//			dto.setGoal(goalMapper.convertToDto(goalDao.findFirst1ByOrderById()));
//			dto.setWasteWeight(0.0);
//			dto.setWasteTypeId(wasteTypeMapper.convertToDto(wasteTypeDao.findById(accompPasspSaveDto.getWasteTypeIdList().get(waste)).get()));
//			AccompPasspWaste newWaste = accompPasspWasteMapper.convertToEntity(dto);
//			newWaste.setAccompPassps(ap);
//			accompPasspWasteService.save(newWaste);
//		}

	}

	@Override
	public void newAccopmPassp(AccompPasspSaveDto accompPasspSaveDto) {
		AccompPasspDto temp = new AccompPasspDto();
		accompPasspSaveDto.getWasteTypeIdList();
		temp.setAccompPasspDate(accompPasspSaveDto.getAccompPasspDate());
		temp.setBoxing(accompPasspSaveDto.getBoxing());
		temp.setCarModel(accompPasspSaveDto.getCarModel());
		temp.setCarNumber(accompPasspSaveDto.getCarNumber());
		Organization carrierOrg = organizationDao.findById(accompPasspSaveDto.getCarrierOrganizationId()).get();
		temp.setCarrierOrganizationId(carrierOrg.getId());
		temp.setCarrierOrganizationAddress(carrierOrg.getAddress());
		temp.setCarrierOrganizationName(carrierOrg.getName());
		Contract contract = contractDao.findById(accompPasspSaveDto.getContractId()).get();
		temp.setContractDate(contract.getContractDate().toString());
		temp.setContractId(contract.getId());
		temp.setContractNumber(contract.getNumber());
		temp.setDriverFio(accompPasspSaveDto.getDriverFio());
		temp.setNumber(accompPasspSaveDto.getNumber());
		temp.setTransportationDate(accompPasspSaveDto.getTransportationDate());
		temp.setRecipientOrganizationAddress(contract.getOrganization().getAddress());
		temp.setRecipientOrganizationName(contract.getOrganization().getName());
		temp.setWasteDestination(contract.getOrganization().getWasteDestination());
		temp.setAddress(accompPasspSaveDto.getAddress());
		temp.setWasteTypeIdList(new ArrayList<>());
		temp.setDepartmentDtos(new ArrayList<>());
		AccompPassp ap = accompPasspMapper.convertToEntity(temp);
		ap = accompPasspDao.save(ap);
		for (int waste = 0; waste<accompPasspSaveDto.getDepartmentList().stream().count();waste++){
			AccompPasspDepartmentDto accompPasspDepartmentDto = new AccompPasspDepartmentDto();
			accompPasspDepartmentDto.setDepartment(departmentMapper.convertToDto(departmentDao.findById(accompPasspSaveDto.getDepartmentList().get(waste)).get()));
			AccompPasspDepartment newWaste = accompPasspDepartmentMapp.convertToEntity(accompPasspDepartmentDto);
			newWaste.setAccompPassps(ap);
			accompPasspDepartmentService.save(newWaste);
		}
		for (int waste = 0; waste<accompPasspSaveDto.getWasteTypeIdList().stream().count();waste++){
			AccompPasspWasteDto dto = new AccompPasspWasteDto();
			dto.setGoal(goalMapper.convertToDto(goalDao.findFirst1ByOrderById()));
			dto.setWasteWeight(0.0);
			dto.setWasteTypeId(wasteTypeMapper.convertToDto(wasteTypeDao.findById(accompPasspSaveDto.getWasteTypeIdList().get(waste)).get()));
			AccompPasspWaste newWaste = accompPasspWasteMapper.convertToEntity(dto);
			newWaste.setAccompPassps(ap);
			accompPasspWasteService.save(newWaste);
			newWaste  = accompPasspWasteDao.findById(newWaste.getId()).get();
			pod9OwnWasteService.save(newWaste);
		}

	}



	//
//	@Override
//	public void delete(Integer id) {
//		accompPasspWasteService.delete(id);
//		accompPasspDao.deleteById(id);
//	}
//
	@Override
	public AccompPasspDetailsDto getDetailsById(Integer id) {
		AccompPassp accompPassp = accompPasspDao.getOne(id);
		return accompPasspMapper.convertToDetailsDto(accompPassp);
	}
//
	@Override
	public Integer getNextNumber() {
		AccompPassp accompPassp = accompPasspDao.findFirst1ByOrderByNumberDesc();
		int lastNumber = accompPassp!=null?accompPassp.getNumber():0;
		return ++lastNumber;
	}
//
//	@Override
//	public AccompPasspDto setNextParameters(AccompPasspDto accompPasspDto) {
//		accompPasspDto.setId(null);
//		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//		LocalDate date = LocalDate.now();
//		accompPasspDto.setAccompPasspDate(date.format(dateFormat));
//		accompPasspDto.setWasteWeight(null);
//		accompPasspDto.setNumber(getNextNumber());
//		return accompPasspDto;
//	}
//
	@Override
	public List<String> getYears() {
		return accompPasspDao.findYears();
	}
//
	@Override
	public List<MonthDto> getMonth() {
		List<String> monthNumbers = accompPasspDao.findMonthNumber();
		List<MonthDto> months = new ArrayList<>();
		Locale loc = Locale.forLanguageTag("Ru");
		for (String monthNumber: monthNumbers) {
			String monthName = Month.of(Integer.parseInt(monthNumber)).getDisplayName(TextStyle.FULL_STANDALONE, loc);
			months.add(new MonthDto(monthNumber, monthName));
		}
		return months;
	}

	@Override
	public void saveWeightAndGoal(Integer accompPasspWaste, Integer goalId, double weight) {
		AccompPasspWaste temp = accompPasspWasteDao.findById(accompPasspWaste).get();
		temp.setGoal(goalDao.findById(goalId).get());
		temp.setWasteWeight(weight);
		accompPasspWasteDao.save(temp);
	}
//
//	@Override
//	public List<AccompPasspWeightDto> getAllAPWeight() {
//		List<AccompPassp> accompPassps = accompPasspDao.getAllWeightNull();
//		return accompPasspMapper.convertToWieghtDtoList(accompPassps);
//	}
//
//	@Override
//	public AccompPasspDto setWeightAndGoal(Integer id, Integer goalId, Double weight) {
//		AccompPasspDto accompPasspDto = getOne(id);
//		if (weight < 0 ) {
//			weight = null;
//		}
//		accompPasspDto.setGoalId(goalId);
//		accompPasspDto.setWasteWeight(weight);
//		return accompPasspDto;
//	}
//
//	@Override
//	public List<Pod10Dto> getAccompPasspByMonth(String month) {
//		List<AccompPassp> accompPassps = accompPasspDao.findAllByMonth(month);
//		List<Pod10Dto> pod10Dtos = accompPasspMapper.convertToPasspDtoList(accompPassps);
//		for (Pod10Dto pod10Dto: pod10Dtos) {
//			switch (pod10Dto.getGoalName()) {
//				case "Использование": pod10Dto.setTransferredUsed(pod10Dto.getWasteGenerate());
//					break;
//				case "Обезвреживание": pod10Dto.setTransferredNeutralized(pod10Dto.getWasteGenerate());
//					break;
//				case "Хранение": pod10Dto.setTransferredStored(pod10Dto.getWasteGenerate());
//					break;
//				case "Захоронение": pod10Dto.setTransferredBuried(pod10Dto.getWasteGenerate());
//					break;
//			}
//		}
//		return pod10Dtos;
//	}
}
