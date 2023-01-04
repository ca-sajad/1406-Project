package search_engine.model;

import java.io.File;

public enum Constants {
    BASE_DIR("src" + File.separator + "search_engine" + File.separator + "data"),
    IDF_FOLDER_NAME(BASE_DIR.getName() + File.separator + "IDF"),
    URLS_FILE_NAME(BASE_DIR.getName() + File.separator + "URLs"),
    URLS_IN_FILE_NAME( "incoming links.txt"),
    URLS_OUT_FILE_NAME( "outgoing links.txt"),
    TF_FOLDER_NAME("TF"),
    TF_IDF_FOLDER_NAME("TF_IDF"),
    PAGE_RANK_FILE_NAME("page_rank.txt"),
    TITLE_FILE_NAME("title.txt"),
    STYLESHEET("src" + File.separator + "search_engine" + File.separator + "gui" + File.separator + "style.css"),
    CHERRY_PIC("resources" + File.separator + "cherry.jpg"),


    TITLE_TAG_START("<title>"),
    TITLE_TAG_END("</title>"),
    HREF_START("href=\""),
    LINK_TAG_START("<a"),
    LINK_TAG_END("</a>"),
    QUOTATION("\""),
    PARA_TAG_START("<p>"),
    PARA_TAG_END("</p>"),
    GT_SIGN(">"),
    LT_SIGN("<"),
    DOT("."),
    NEW_LINE("\n"),
    SLASH("/"),
    SPACE(" "),

    ALPHA("0.1"),
    THRESHOLD("0.0001"),
    TOP_SIMILARITIES("10"),
    ;

    private final String constant;

    Constants(String constant) {
        this.constant = constant;
    }

    public String getName() {
        return constant;
    }
}
