package uo.ri.cws.application.service.intervention;

import java.time.LocalDateTime;

/**
 * This service is intended to be used by the Mechanic It follows the ISP
 * principle (@see SOLID principles from RC Martin)
 */
public interface InterventionService {

    // ...

	public static class InterventionDto {

		public String id;
		public long version;

		public LocalDateTime date;
		public int minutes;

		// might be null
		public String mechanicId;
		public String workorderId;

	}
}
