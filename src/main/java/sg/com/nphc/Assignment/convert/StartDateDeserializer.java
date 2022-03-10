package sg.com.nphc.Assignment.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import sg.com.nphc.Assignment.exception.BadInputException;
import sg.com.nphc.Assignment.util.Helper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StartDateDeserializer extends StdDeserializer<LocalDate> {

    public StartDateDeserializer() {
        this(null);
    }

    private StartDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonparser, DeserializationContext deserializationContext) throws IOException {
        String date = jsonparser.getText();

        for (DateTimeFormatter formatter : Helper.getStartDateFormatters()) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException ex) {
            }
        }
        throw new BadInputException("Invalid date");
    }
}
