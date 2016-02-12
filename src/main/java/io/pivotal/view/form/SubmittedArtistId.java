package io.pivotal.view.form;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SubmittedArtistId {

    @Size(min=1, max=10)
    private String discogId;
}