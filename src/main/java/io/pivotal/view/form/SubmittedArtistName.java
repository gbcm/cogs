package io.pivotal.view.form;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SubmittedArtistName {

    @Size(min=2, max=100)
    private String artistName;
}