import lombok.Data;

@Data
public class Usage {

    private String promptTokens;

    private String completionTokens;

    private String totalTokens;

    private String duration;

    private String imageCost;

    private String inputImages;

    private String costLevel;

}
