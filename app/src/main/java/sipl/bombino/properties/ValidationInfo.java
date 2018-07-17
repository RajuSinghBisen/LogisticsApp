package sipl.bombino.properties;

public class ValidationInfo {

	int id;
	String validationField;
	int isValidationRequired;

	public String getValidationField() {
		return validationField;
	}

	public void setValidationField(String validationField) {
		this.validationField = validationField;
	}

	public int getIsValidationRequired() {
		return isValidationRequired;
	}

	public void setIsValidationRequired(int isValidationRequired) {
		this.isValidationRequired = isValidationRequired;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
