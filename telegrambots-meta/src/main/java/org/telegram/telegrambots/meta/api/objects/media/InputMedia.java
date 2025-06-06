package org.telegram.telegrambots.meta.api.objects.media;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.interfaces.Validable;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author Ruben Bermudez
 * @version 3.5
 */
//@JsonSerialize(using = InputMediaSerializer.class)
//@JsonDeserialize(using = InputMediaDeserializer.class)
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = Void.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = InputMediaAnimation.class, name = "animation"),
        @JsonSubTypes.Type(value = InputMediaAudio.class, name = "audio"),
        @JsonSubTypes.Type(value = InputMediaDocument.class, name = "document"),
        @JsonSubTypes.Type(value = InputMediaPhoto.class, name = "photo"),
        @JsonSubTypes.Type(value = InputMediaVideo.class, name = "video")
})
public abstract class InputMedia implements Validable, BotApiObject {
    public static final String TYPE_FIELD = "type";
    public static final String MEDIA_FIELD = "media";
    public static final String CAPTION_FIELD = "caption";
    public static final String PARSE_MODE_FIELD = "parse_mode";
    public static final String CAPTION_ENTITIES_FIELD = "caption_entities";

    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended),
     * pass an HTTP URL for Telegram to get a file from the Internet, or pass "attach://&lt;file_attach_name&gt;"
     * to upload a new one using multipart/form-data under &lt;file_attach_name&gt; name.
     */
    @JsonProperty(MEDIA_FIELD)
    @NonNull
    private String media;
    /**
     * Optional.
     * Caption of the media to be sent, 0-200 characters
     */
    @JsonProperty(CAPTION_FIELD)
    private String caption;
    /**
     * Optional.
     * Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption.
     */
    @JsonProperty(PARSE_MODE_FIELD)
    private String parseMode;
    /**
     * Optional.
     * List of special entities that appear in message text, which can be specified instead of parse_mode
     */
    @JsonProperty(CAPTION_ENTITIES_FIELD)
    @Singular
    private List<MessageEntity> captionEntities;
    /**
     * True to upload a new media, false to use a fileId or URL
     */
    @JsonIgnore
    private boolean isNewMedia;
    /**
     * Name of the media to upload
     */
    @JsonIgnore
    private String mediaName;
    /**
     * New media file
     */
    @JsonIgnore
    private File newMediaFile;
    /**
     * New media stream
     */
    @JsonIgnore
    private InputStream newMediaStream;

    @JsonIgnore
    public boolean isNewMedia() {
        return isNewMedia;
    }

    /**
     * Use this setter to send an existing file (using file_id) or an url.
     *
     * @param media File_id or URL of the file to send
     */
    public void setMedia(String media) {
        this.media = media;
        this.isNewMedia = false;
    }

    /**
     * Use this setter to send new file.
     *
     * @param mediaFile File to send
     */
    public void setMedia(File mediaFile, String fileName) {
        this.newMediaFile = mediaFile;
        this.isNewMedia = true;
        this.mediaName = fileName;
        this.media = "attach://" + fileName;
    }

    /**
     * Use this setter to send new file as stream.
     *
     * @param mediaStream File to send
     */
    public void setMedia(InputStream mediaStream, String fileName) {
        this.newMediaStream = mediaStream;
        this.isNewMedia = true;
        this.mediaName = fileName;
        this.media = "attach://" + fileName;
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (isNewMedia) {
            if (mediaName == null || mediaName.isEmpty()) {
                throw new TelegramApiValidationException("Media name can't be empty", this);
            }
            if (newMediaFile == null && newMediaStream == null) {
                throw new TelegramApiValidationException("Media can't be empty", this);
            }
        } else if (media.isEmpty()) {
            throw new TelegramApiValidationException("Media can't be empty", this);
        }
        if (parseMode != null && (captionEntities != null && !captionEntities.isEmpty())) {
            throw new TelegramApiValidationException("Parse mode can't be enabled if Entities are provided", this);
        }
    }

    @JsonProperty(TYPE_FIELD)
    public abstract String getType();

    public static abstract class InputMediaBuilder<C extends InputMedia, B extends InputMediaBuilder<C, B>> {
        @JsonIgnore
        public B media(@NonNull File mediaFile, @NonNull String fileName) {
            this.newMediaFile = mediaFile;
            this.isNewMedia = true;
            this.mediaName = fileName;
            this.media = "attach://" + fileName;
            return self();
        }

        @JsonIgnore
        public B media(@NonNull InputStream mediaStream, @NonNull String fileName) {
            this.newMediaStream = mediaStream;
            this.isNewMedia = true;
            this.mediaName = fileName;
            this.media = "attach://" + fileName;
            return self();
        }

        @JsonProperty(MEDIA_FIELD)
        public B media(@NonNull String media) {
            this.media = media;
            this.isNewMedia = false;
            return self();
        }

        // This method are overriding to avoid lombok to create them as public
        @JsonIgnore
        private B isNewMedia(boolean isNewMedia) {
            return self();
        }

        @JsonIgnore
        private B mediaName(String mediaName) {
            return self();
        }

        @JsonIgnore
        private B newMediaFile(File newMediaFile) {
            return self();
        }

        @JsonIgnore
        private B newMediaStream(InputStream newMediaStream) {
            return self();
        }
    }
}
