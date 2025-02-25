package bhagya.festivo.festivo.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import bhagya.festivo.festivo.service.VendorCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the vendorCategoryName value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = VendorCategoryVendorCategoryNameUnique.VendorCategoryVendorCategoryNameUniqueValidator.class
)
public @interface VendorCategoryVendorCategoryNameUnique {

    String message() default "{Exists.vendorCategory.vendorCategoryName}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class VendorCategoryVendorCategoryNameUniqueValidator implements ConstraintValidator<VendorCategoryVendorCategoryNameUnique, String> {

        private final VendorCategoryService vendorCategoryService;
        private final HttpServletRequest request;

        public VendorCategoryVendorCategoryNameUniqueValidator(
                final VendorCategoryService vendorCategoryService,
                final HttpServletRequest request) {
            this.vendorCategoryService = vendorCategoryService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("vendorCategoryId");
            if (currentId != null && value.equalsIgnoreCase(vendorCategoryService.get(UUID.fromString(currentId)).getVendorCategoryName())) {
                // value hasn't changed
                return true;
            }
            return !vendorCategoryService.vendorCategoryNameExists(value);
        }

    }

}
