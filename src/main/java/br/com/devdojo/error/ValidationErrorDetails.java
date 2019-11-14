package br.com.devdojo.error;

public class ValidationErrorDetails extends ErrorDetail {

    private String fields;
    private String fieldsMessages;

    public void setFields(String fields) {
        this.fields = fields;
    }

    public void setFieldsMessages(String fieldsMessages) {
        this.fieldsMessages = fieldsMessages;
    }

    public static final class Builder {
        private String title;
        private int status;
        private String detail;
        private Long timestamp;
        private String developerMessage;
        private String fields;
        private String fieldsMessages;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public Builder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder developerMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        public Builder fields(String fields) {
            this.fields = fields;
            return this;
        }

        public Builder fieldsMessages(String fieldsMessages) {
            this.fieldsMessages = fieldsMessages;
            return this;
        }

        public ValidationErrorDetails build() {
            ValidationErrorDetails validationErrorDetails = new ValidationErrorDetails();
            validationErrorDetails.setTitle(title);
            validationErrorDetails.setStatus(status);
            validationErrorDetails.setDetail(detail);
            validationErrorDetails.setFields(fields);
            validationErrorDetails.setFieldsMessages(fieldsMessages);
            validationErrorDetails.setTimestamp(timestamp);
            validationErrorDetails.setDeveloperMessage(developerMessage);
            return validationErrorDetails;
        }
    }
}
