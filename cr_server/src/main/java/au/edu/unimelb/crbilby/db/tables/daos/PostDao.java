/*
 * This file is generated by jOOQ.
 */
package au.edu.unimelb.crbilby.db.tables.daos;


import au.edu.unimelb.crbilby.db.tables.Post;
import au.edu.unimelb.crbilby.db.tables.records.PostRecord;

import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * The table <code>cr_db.post</code>.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PostDao extends DAOImpl<PostRecord, au.edu.unimelb.crbilby.db.tables.pojos.Post, Integer> {

    /**
     * Create a new PostDao without any configuration
     */
    public PostDao() {
        super(Post.POST, au.edu.unimelb.crbilby.db.tables.pojos.Post.class);
    }

    /**
     * Create a new PostDao with an attached configuration
     */
    public PostDao(Configuration configuration) {
        super(Post.POST, au.edu.unimelb.crbilby.db.tables.pojos.Post.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getId(au.edu.unimelb.crbilby.db.tables.pojos.Post object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<au.edu.unimelb.crbilby.db.tables.pojos.Post> fetchById(Integer... values) {
        return fetch(Post.POST.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public au.edu.unimelb.crbilby.db.tables.pojos.Post fetchOneById(Integer value) {
        return fetchOne(Post.POST.ID, value);
    }

    /**
     * Fetch records that have <code>name IN (values)</code>
     */
    public List<au.edu.unimelb.crbilby.db.tables.pojos.Post> fetchByName(String... values) {
        return fetch(Post.POST.NAME, values);
    }

    /**
     * Fetch records that have <code>escalate_lvl IN (values)</code>
     */
    public List<au.edu.unimelb.crbilby.db.tables.pojos.Post> fetchByEscalateLvl(Integer... values) {
        return fetch(Post.POST.ESCALATE_LVL, values);
    }

    /**
     * Fetch records that have <code>chief IN (values)</code>
     */
    public List<au.edu.unimelb.crbilby.db.tables.pojos.Post> fetchByChief(String... values) {
        return fetch(Post.POST.CHIEF, values);
    }
}
