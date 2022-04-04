import com.farecalculator.exception.ApplicationException;
import com.farecalculator.processors.DailyCapFareRule;
import com.farecalculator.processors.PeakOffPeakFareRule;
import com.farecalculator.processors.RuleProcessor;
import com.farecalculator.processors.WeeklyCapFareRule;
import com.farecalculator.service.FareCalculatorService;
import com.farecalculator.service.FareController;
import com.farecalculator.validator.DefaultValidator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

public class ValidationsTest {

  @Rule public ExpectedException expected = ExpectedException.none();

  @Test
  public void testDateFormatValidation() throws ApplicationException {
    expected.expect(ApplicationException.class);
    expected.expectMessage(
        "Invalid Date format - expected format - dd-MM-yyyy for Journey : 01-13-2022 18:15 1 1");
    RuleProcessor peakOffPeakFareRule =
        new PeakOffPeakFareRule(new DailyCapFareRule(new WeeklyCapFareRule(null)));
    List<String> journeys =
        Arrays.asList("01-04-2022 10:20 1 1", "01-04-2022 10:25 2 1", "01-13-2022 18:15 1 1");
    callService(journeys);
  }

  @Test
  public void tesZoneValidation() throws ApplicationException {
    expected.expect(ApplicationException.class);
    expected.expectMessage("From Zone - 3 is not a valid for Journey - 01-04-2022 10:25 3 1");

    List<String> journeys =
        Arrays.asList("01-04-2022 10:20 1 1", "01-04-2022 10:25 3 1", "01-04-2022 18:15 1 1");
    callService(journeys);
  }

  private void callService(List<String> journeys)
      throws ApplicationException {
      RuleProcessor peakOffPeakFareRule =
              new PeakOffPeakFareRule(null);
    FareCalculatorService service = new FareCalculatorService(peakOffPeakFareRule);
    FareController controller = new FareController(service);
    controller.setValidator(new DefaultValidator());
    controller.getFare(journeys);
  }

  @Test
  public void tesTimeValidation() throws ApplicationException {
    expected.expect(ApplicationException.class);
    expected.expectMessage(
        "Invalid Time format - expected format - HH:mm for Journey : 01-04-2022 33:20 1 1");
    RuleProcessor peakOffPeakFareRule =
        new PeakOffPeakFareRule(new DailyCapFareRule(new WeeklyCapFareRule(null)));
    List<String> journeys =
        Arrays.asList("01-04-2022 33:20 1 1", "01-04-2022 10:25 2 1", "01-04-2022 18:15 1 1");
    callService(journeys);
  }
}
