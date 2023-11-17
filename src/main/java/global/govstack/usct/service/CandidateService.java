package global.govstack.usct.service;

import global.govstack.usct.configuration.OpenImisProperties;
import global.govstack.usct.controller.dto.CandidateDto;
import global.govstack.usct.controller.dto.ConsentDto;
import global.govstack.usct.controller.dto.CreateCandidateDto;
import global.govstack.usct.controller.dto.CreatePersonDto;
import global.govstack.usct.controller.dto.digital.registries.PackageDto;
import global.govstack.usct.model.Candidate;
import global.govstack.usct.model.Person;
import global.govstack.usct.repositories.CandidateRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {
  private final CandidateRepository candidateRepository;
  private final PersonService personService;
  private final PackageService packageService;
  private final ConsentService consentService;
  private final OpenImisProperties openImisProperties;

  public CandidateService(
      CandidateRepository candidateRepository,
      PersonService personService,
      PackageService packageService,
      ConsentService consentService,
      OpenImisProperties openImisProperties) {
    this.candidateRepository = candidateRepository;
    this.personService = personService;
    this.packageService = packageService;
    this.consentService = consentService;
    this.openImisProperties = openImisProperties;
  }

  public List<CandidateDto> findAll() {
    List<Candidate> candidates = candidateRepository.findAll();
    return candidates.stream()
        .map(
            candidate -> {
              List<PackageDto> packagesDto = List.of();
              if (openImisProperties.mode().equals("emulator")) {
                packagesDto =
                    candidate.getEmulatorPackageIds().stream()
                        .map(packageService::getById)
                        .toList();
              }
              if (openImisProperties.mode().equals("open-imis")) {
                packagesDto =
                    candidate.getOpenImisPackageIds().stream()
                        .map(packageService::getById)
                        .toList();
              }
              var consent = new ConsentDto(consentService.findById(candidate.getId()));
              return new CandidateDto(candidate, packagesDto, consent);
            })
        .toList();
  }

  public CandidateDto findById(int id) {
    Candidate candidate =
        candidateRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate with id: " + id + " doesn't exist"));
    List<PackageDto> packageDtoList =
        candidate.getOpenImisPackageIds().stream().map(packageService::getById).toList();
    var consent = new ConsentDto(consentService.findById(candidate.getId()));
    return new CandidateDto(candidate, packageDtoList, consent);
  }

  public void deleteById(Integer id) {
    candidateRepository.deleteById(id);
  }

  @Transactional
  public Candidate save(CreateCandidateDto createCandidateDto) {
    CreatePersonDto createPersonDto = createCandidateDto.person();
    Person person = personService.save(createPersonDto);
    Candidate candidate = new Candidate();
    candidate.setPerson(person);
    candidate.setOpenImisPackageIds(createCandidateDto.packageIds());
    return candidateRepository.save(candidate);
  }

  @Transactional
  public Candidate save(Candidate candidate) {
    personService.save(candidate.getPerson());
    return candidateRepository.save(candidate);
  }
}
