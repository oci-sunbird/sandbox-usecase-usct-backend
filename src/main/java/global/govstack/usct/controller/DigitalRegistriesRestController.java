package global.govstack.usct.controller;

import global.govstack.usct.service.DigitalRegistriesEmulator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/digitalregistries")
@CrossOrigin
@PreAuthorize("hasAnyRole('PAYMENT_OFFICER','ENROLLMENT_OFFICER','REGISTRY_OFFICER')")
public class DigitalRegistriesRestController {
  private final DigitalRegistriesEmulator digitalRegistriesService;

  public DigitalRegistriesRestController(DigitalRegistriesEmulator digitalRegistriesService) {
    this.digitalRegistriesService = digitalRegistriesService;
  }

  @GetMapping("/emulator-health")
  public String getHealth() {
    return digitalRegistriesService.health();
  }
}
