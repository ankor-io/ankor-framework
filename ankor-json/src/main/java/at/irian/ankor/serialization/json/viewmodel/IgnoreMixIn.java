package at.irian.ankor.serialization.json.viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

/**
 * MixIn dummy class to mix in @JsonIgnoreType to certain third-party types.
 *
 * @author Manfred Geiler
 */
@JsonIgnoreType
interface IgnoreMixIn {}
