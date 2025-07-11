package org.telegram.telegrambots.meta.api.objects.inlinequery.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputMessageContent;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * Represents a link to an article or web page.
 */

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class InlineQueryResultArticle implements InlineQueryResult {

    private static final String TYPE_FIELD = "type";
    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String INPUT_MESSAGE_CONTENT_FIELD = "input_message_content";
    private static final String REPLY_MARKUP_FIELD = "reply_markup";
    private static final String URL_FIELD = "url";
    private static final String HIDE_URL_FIELD = "hide_url";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String THUMBNAIL_URL_FIELD = "thumbnail_url";
    private static final String THUMBNAIL_WIDTH_FIELD = "thumbnail_width";
    private static final String THUMBNAIL_HEIGHT_FIELD = "thumbnail_height";

    /**
     * Type of the result, must be “article”
     */
    @JsonProperty(TYPE_FIELD)
    private final String type = "article";
    /**
     * Unique identifier of this result, 1-64 bytes
     */
    @JsonProperty(ID_FIELD)
    @NonNull
    private String id;
    /**
     * Title of the result
     */
    @JsonProperty(TITLE_FIELD)
    @NonNull
    private String title;
    /**
     * Content of the message to be sent
     */
    @JsonProperty(INPUT_MESSAGE_CONTENT_FIELD)
    @NonNull
    private InputMessageContent inputMessageContent;
    /**
     * Optional.
     * Inline keyboard attached to the message
     */
    @JsonProperty(REPLY_MARKUP_FIELD)
    private InlineKeyboardMarkup replyMarkup;
    /**
     * Optional.
     * URL of the result
     */
    @JsonProperty(URL_FIELD)
    private String url;
    /**
     * Optional.
     * Short description of the result
     */
    @JsonProperty(DESCRIPTION_FIELD)
    private String description;
    /**
     * Optional.
     * Url of the thumbnail for the result
     */
    @JsonProperty(THUMBNAIL_URL_FIELD)
    private String thumbnailUrl;
    /**
     * Optional.
     * Thumbnail width
     */
    @JsonProperty(THUMBNAIL_WIDTH_FIELD)
    private Integer thumbnailWidth;
    /**
     * Optional.
     * Thumbnail height
     */
    @JsonProperty(THUMBNAIL_HEIGHT_FIELD)
    private Integer thumbnailHeight;

    @Override
    public void validate() throws TelegramApiValidationException {
        if (id.isEmpty()) {
            throw new TelegramApiValidationException("ID parameter can't be empty", this);
        }
        if (title.isEmpty()) {
            throw new TelegramApiValidationException("Title parameter can't be empty", this);
        }

        inputMessageContent.validate();

        if (replyMarkup != null) {
            replyMarkup.validate();
        }
    }
}
