/*
 * This file is generated by jOOQ.
 */
package au.edu.unimelb.crbilby.db.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;


/**
 * The table <code>cr_db.case_assign</code>.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CaseAssign implements Serializable {

    private static final long serialVersionUID = -899234497;

    private Integer case_;
    private String  profile;

    public CaseAssign() {}

    public CaseAssign(CaseAssign value) {
        this.case_ = value.case_;
        this.profile = value.profile;
    }

    public CaseAssign(
        Integer case_,
        String  profile
    ) {
        this.case_ = case_;
        this.profile = profile;
    }

    public Integer getCase() {
        return this.case_;
    }

    public void setCase(Integer case_) {
        this.case_ = case_;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CaseAssign other = (CaseAssign) obj;
        if (case_ == null) {
            if (other.case_ != null)
                return false;
        }
        else if (!case_.equals(other.case_))
            return false;
        if (profile == null) {
            if (other.profile != null)
                return false;
        }
        else if (!profile.equals(other.profile))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.case_ == null) ? 0 : this.case_.hashCode());
        result = prime * result + ((this.profile == null) ? 0 : this.profile.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CaseAssign (");

        sb.append(case_);
        sb.append(", ").append(profile);

        sb.append(")");
        return sb.toString();
    }
}
