package com.ssafy.edu.board.model;

import java.util.Date;

public class Board {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column spring_jwt..boards.board_id
     *
     * @mbg.generated
     */
    private Integer boardId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column spring_jwt..boards.member_id
     *
     * @mbg.generated
     */
    private Integer memberId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column spring_jwt..boards.title
     *
     * @mbg.generated
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column spring_jwt..boards.boardcontent
     *
     * @mbg.generated
     */
    private String boardcontent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column spring_jwt..boards.writedate
     *
     * @mbg.generated
     */
    private Date writedate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column spring_jwt..boards.hit
     *
     * @mbg.generated
     */
    private Integer hit;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column spring_jwt..boards.board_id
     *
     * @return the value of spring_jwt..boards.board_id
     *
     * @mbg.generated
     */
    public Integer getBoardId() {
        return boardId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column spring_jwt..boards.board_id
     *
     * @param boardId the value for spring_jwt..boards.board_id
     *
     * @mbg.generated
     */
    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column spring_jwt..boards.member_id
     *
     * @return the value of spring_jwt..boards.member_id
     *
     * @mbg.generated
     */
    public Integer getMemberId() {
        return memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column spring_jwt..boards.member_id
     *
     * @param memberId the value for spring_jwt..boards.member_id
     *
     * @mbg.generated
     */
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column spring_jwt..boards.title
     *
     * @return the value of spring_jwt..boards.title
     *
     * @mbg.generated
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column spring_jwt..boards.title
     *
     * @param title the value for spring_jwt..boards.title
     *
     * @mbg.generated
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column spring_jwt..boards.boardcontent
     *
     * @return the value of spring_jwt..boards.boardcontent
     *
     * @mbg.generated
     */
    public String getBoardcontent() {
        return boardcontent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column spring_jwt..boards.boardcontent
     *
     * @param boardcontent the value for spring_jwt..boards.boardcontent
     *
     * @mbg.generated
     */
    public void setBoardcontent(String boardcontent) {
        this.boardcontent = boardcontent == null ? null : boardcontent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column spring_jwt..boards.writedate
     *
     * @return the value of spring_jwt..boards.writedate
     *
     * @mbg.generated
     */
    public Date getWritedate() {
        return writedate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column spring_jwt..boards.writedate
     *
     * @param writedate the value for spring_jwt..boards.writedate
     *
     * @mbg.generated
     */
    public void setWritedate(Date writedate) {
        this.writedate = writedate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column spring_jwt..boards.hit
     *
     * @return the value of spring_jwt..boards.hit
     *
     * @mbg.generated
     */
    public Integer getHit() {
        return hit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column spring_jwt..boards.hit
     *
     * @param hit the value for spring_jwt..boards.hit
     *
     * @mbg.generated
     */
    public void setHit(Integer hit) {
        this.hit = hit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table spring_jwt..boards
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", boardId=").append(boardId);
        sb.append(", memberId=").append(memberId);
        sb.append(", title=").append(title);
        sb.append(", boardcontent=").append(boardcontent);
        sb.append(", writedate=").append(writedate);
        sb.append(", hit=").append(hit);
        sb.append("]");
        return sb.toString();
    }
}