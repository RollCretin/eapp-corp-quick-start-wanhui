/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: GitlabHockModel
 * Author:   a112233
 * Date:     2019/11/6 5:27 PM
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.model;

import java.util.List;

/**
 * 〈〉
 *
 * @author a112233
 * @create 2019/11/6
 * @since 1.0.0
 */
public class GitlabHockModel {

    /**
     * object_kind : merge_request
     * event_type : merge_request
     * user : {"name":"muxiannian","username":"muxiannian","avatar_url":"https://secure.gravatar.com/avatar/c6828b1055af060a2a3156b39a9f7264?s=80&d=identicon"}
     * project : {"id":1025,"name":"FollowmeAndroidWithComponent","description":"组件化架构的android项目","web_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent","avatar_url":null,"git_ssh_url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","git_http_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git","namespace":"jichengzhi","visibility_level":0,"path_with_namespace":"jichengzhi/FollowmeAndroidWithComponent","default_branch":"master","ci_config_path":null,"homepage":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent","url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","ssh_url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","http_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git"}
     * object_attributes : {"assignee_id":202,"author_id":134,"created_at":"2019-11-05 02:48:40 UTC","description":"","head_pipeline_id":null,"id":12104,"iid":58,"last_edited_at":null,"last_edited_by_id":null,"merge_commit_sha":null,"merge_error":null,"merge_params":{"force_remove_source_branch":"0"},"merge_status":"unchecked","merge_user_id":null,"merge_when_pipeline_succeeds":false,"milestone_id":null,"source_branch":"dev-im_crm-mxn","source_project_id":1025,"state":"opened","target_branch":"dev-im_crm-mxn-pre","target_project_id":1025,"time_estimate":0,"title":"IM-CRM版本第三次Merge Request","updated_at":"2019-11-05 03:01:19 UTC","updated_by_id":null,"url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent/merge_requests/58","source":{"id":1025,"name":"FollowmeAndroidWithComponent","description":"组件化架构的android项目","web_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent","avatar_url":null,"git_ssh_url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","git_http_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git","namespace":"jichengzhi","visibility_level":0,"path_with_namespace":"jichengzhi/FollowmeAndroidWithComponent","default_branch":"master","ci_config_path":null,"homepage":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent","url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","ssh_url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","http_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git"},"target":{"id":1025,"name":"FollowmeAndroidWithComponent","description":"组件化架构的android项目","web_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent","avatar_url":null,"git_ssh_url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","git_http_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git","namespace":"jichengzhi","visibility_level":0,"path_with_namespace":"jichengzhi/FollowmeAndroidWithComponent","default_branch":"master","ci_config_path":null,"homepage":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent","url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","ssh_url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","http_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git"},"last_commit":{"id":"962c7fc6487fa815080d955287b7e14cfcdcc9e7","message":"feat：同步翻译\n","timestamp":"2019-11-05T03:01:04Z","url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent/commit/962c7fc6487fa815080d955287b7e14cfcdcc9e7","author":{"name":"muxiannian","email":"muxiannian@followme.cn"}},"work_in_progress":false,"total_time_spent":0,"human_total_time_spent":null,"human_time_estimate":null,"action":"update","oldrev":"be922ddbbf000947c4c414b249de7c94e36b81e5"}
     * labels : [{"id":110,"title":"IM-CRM","color":"#F0AD4E","project_id":1025,"created_at":"2019-10-28 10:50:54 UTC","updated_at":"2019-10-28 10:50:54 UTC","template":false,"description":null,"type":"ProjectLabel","group_id":null}]
     * changes : {"assignee":{"previous":null,"current":{"name":"高爽","username":"gaoshuang","avatar_url":"https://secure.gravatar.com/avatar/ae5e5a13c541fb492da552e8afa23b8c?s=80&d=identicon"}},"labels":{"previous":[],"current":[{"id":110,"title":"IM-CRM","color":"#F0AD4E","project_id":1025,"created_at":"2019-10-28 10:50:54 UTC","updated_at":"2019-10-28 10:50:54 UTC","template":false,"description":null,"type":"ProjectLabel","group_id":null}]},"total_time_spent":{"previous":null,"current":0}}
     * repository : {"name":"FollowmeAndroidWithComponent","url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","description":"组件化架构的android项目","homepage":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent"}
     * assignee : {"name":"高爽","username":"gaoshuang","avatar_url":"https://secure.gravatar.com/avatar/ae5e5a13c541fb492da552e8afa23b8c?s=80&d=identicon"}
     */

    private String object_kind;
    private String event_type;
    private UserBean user;
    private ProjectBean project;
    private ObjectAttributesBean object_attributes;
    private ChangesBean changes;
    private RepositoryBean repository;
    private AssigneeBeanX assignee;
    private List<LabelsBeanX> labels;

    public String getObject_kind() {
        return object_kind;
    }

    public void setObject_kind(String object_kind) {
        this.object_kind = object_kind;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public ProjectBean getProject() {
        return project;
    }

    public void setProject(ProjectBean project) {
        this.project = project;
    }

    public ObjectAttributesBean getObject_attributes() {
        return object_attributes;
    }

    public void setObject_attributes(ObjectAttributesBean object_attributes) {
        this.object_attributes = object_attributes;
    }

    public ChangesBean getChanges() {
        return changes;
    }

    public void setChanges(ChangesBean changes) {
        this.changes = changes;
    }

    public RepositoryBean getRepository() {
        return repository;
    }

    public void setRepository(RepositoryBean repository) {
        this.repository = repository;
    }

    public AssigneeBeanX getAssignee() {
        return assignee;
    }

    public void setAssignee(AssigneeBeanX assignee) {
        this.assignee = assignee;
    }

    public List<LabelsBeanX> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelsBeanX> labels) {
        this.labels = labels;
    }

    public static class UserBean {
        /**
         * name : muxiannian
         * username : muxiannian
         * avatar_url : https://secure.gravatar.com/avatar/c6828b1055af060a2a3156b39a9f7264?s=80&d=identicon
         */

        private String name;
        private String username;
        private String avatar_url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }
    }

    public static class ProjectBean {
        /**
         * id : 1025
         * name : FollowmeAndroidWithComponent
         * description : 组件化架构的android项目
         * web_url : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent
         * avatar_url : null
         * git_ssh_url : git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git
         * git_http_url : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git
         * namespace : jichengzhi
         * visibility_level : 0
         * path_with_namespace : jichengzhi/FollowmeAndroidWithComponent
         * default_branch : master
         * ci_config_path : null
         * homepage : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent
         * url : git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git
         * ssh_url : git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git
         * http_url : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git
         */

        private int id;
        private String name;
        private String description;
        private String web_url;
        private Object avatar_url;
        private String git_ssh_url;
        private String git_http_url;
        private String namespace;
        private int visibility_level;
        private String path_with_namespace;
        private String default_branch;
        private Object ci_config_path;
        private String homepage;
        private String url;
        private String ssh_url;
        private String http_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public Object getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(Object avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getGit_ssh_url() {
            return git_ssh_url;
        }

        public void setGit_ssh_url(String git_ssh_url) {
            this.git_ssh_url = git_ssh_url;
        }

        public String getGit_http_url() {
            return git_http_url;
        }

        public void setGit_http_url(String git_http_url) {
            this.git_http_url = git_http_url;
        }

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public int getVisibility_level() {
            return visibility_level;
        }

        public void setVisibility_level(int visibility_level) {
            this.visibility_level = visibility_level;
        }

        public String getPath_with_namespace() {
            return path_with_namespace;
        }

        public void setPath_with_namespace(String path_with_namespace) {
            this.path_with_namespace = path_with_namespace;
        }

        public String getDefault_branch() {
            return default_branch;
        }

        public void setDefault_branch(String default_branch) {
            this.default_branch = default_branch;
        }

        public Object getCi_config_path() {
            return ci_config_path;
        }

        public void setCi_config_path(Object ci_config_path) {
            this.ci_config_path = ci_config_path;
        }

        public String getHomepage() {
            return homepage;
        }

        public void setHomepage(String homepage) {
            this.homepage = homepage;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSsh_url() {
            return ssh_url;
        }

        public void setSsh_url(String ssh_url) {
            this.ssh_url = ssh_url;
        }

        public String getHttp_url() {
            return http_url;
        }

        public void setHttp_url(String http_url) {
            this.http_url = http_url;
        }
    }

    public static class ObjectAttributesBean {
        /**
         * assignee_id : 202
         * author_id : 134
         * created_at : 2019-11-05 02:48:40 UTC
         * description :
         * head_pipeline_id : null
         * id : 12104
         * iid : 58
         * last_edited_at : null
         * last_edited_by_id : null
         * merge_commit_sha : null
         * merge_error : null
         * merge_params : {"force_remove_source_branch":"0"}
         * merge_status : unchecked
         * merge_user_id : null
         * merge_when_pipeline_succeeds : false
         * milestone_id : null
         * source_branch : dev-im_crm-mxn
         * source_project_id : 1025
         * state : opened
         * target_branch : dev-im_crm-mxn-pre
         * target_project_id : 1025
         * time_estimate : 0
         * title : IM-CRM版本第三次Merge Request
         * updated_at : 2019-11-05 03:01:19 UTC
         * updated_by_id : null
         * url : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent/merge_requests/58
         * source : {"id":1025,"name":"FollowmeAndroidWithComponent","description":"组件化架构的android项目","web_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent","avatar_url":null,"git_ssh_url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","git_http_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git","namespace":"jichengzhi","visibility_level":0,"path_with_namespace":"jichengzhi/FollowmeAndroidWithComponent","default_branch":"master","ci_config_path":null,"homepage":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent","url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","ssh_url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","http_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git"}
         * target : {"id":1025,"name":"FollowmeAndroidWithComponent","description":"组件化架构的android项目","web_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent","avatar_url":null,"git_ssh_url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","git_http_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git","namespace":"jichengzhi","visibility_level":0,"path_with_namespace":"jichengzhi/FollowmeAndroidWithComponent","default_branch":"master","ci_config_path":null,"homepage":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent","url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","ssh_url":"git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git","http_url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git"}
         * last_commit : {"id":"962c7fc6487fa815080d955287b7e14cfcdcc9e7","message":"feat：同步翻译\n","timestamp":"2019-11-05T03:01:04Z","url":"https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent/commit/962c7fc6487fa815080d955287b7e14cfcdcc9e7","author":{"name":"muxiannian","email":"muxiannian@followme.cn"}}
         * work_in_progress : false
         * total_time_spent : 0
         * human_total_time_spent : null
         * human_time_estimate : null
         * action : update
         * oldrev : be922ddbbf000947c4c414b249de7c94e36b81e5
         */

        private int assignee_id;
        private int author_id;
        private String created_at;
        private String description;
        private Object head_pipeline_id;
        private int id;
        private int iid;
        private Object last_edited_at;
        private Object last_edited_by_id;
        private Object merge_commit_sha;
        private Object merge_error;
        private MergeParamsBean merge_params;
        private String merge_status;
        private Object merge_user_id;
        private boolean merge_when_pipeline_succeeds;
        private Object milestone_id;
        private String source_branch;
        private int source_project_id;
        private String state;
        private String target_branch;
        private int target_project_id;
        private int time_estimate;
        private String title;
        private String updated_at;
        private Object updated_by_id;
        private String url;
        private SourceBean source;
        private TargetBean target;
        private LastCommitBean last_commit;
        private boolean work_in_progress;
        private int total_time_spent;
        private Object human_total_time_spent;
        private Object human_time_estimate;
        private String action;
        private String oldrev;

        public int getAssignee_id() {
            return assignee_id;
        }

        public void setAssignee_id(int assignee_id) {
            this.assignee_id = assignee_id;
        }

        public int getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(int author_id) {
            this.author_id = author_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Object getHead_pipeline_id() {
            return head_pipeline_id;
        }

        public void setHead_pipeline_id(Object head_pipeline_id) {
            this.head_pipeline_id = head_pipeline_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIid() {
            return iid;
        }

        public void setIid(int iid) {
            this.iid = iid;
        }

        public Object getLast_edited_at() {
            return last_edited_at;
        }

        public void setLast_edited_at(Object last_edited_at) {
            this.last_edited_at = last_edited_at;
        }

        public Object getLast_edited_by_id() {
            return last_edited_by_id;
        }

        public void setLast_edited_by_id(Object last_edited_by_id) {
            this.last_edited_by_id = last_edited_by_id;
        }

        public Object getMerge_commit_sha() {
            return merge_commit_sha;
        }

        public void setMerge_commit_sha(Object merge_commit_sha) {
            this.merge_commit_sha = merge_commit_sha;
        }

        public Object getMerge_error() {
            return merge_error;
        }

        public void setMerge_error(Object merge_error) {
            this.merge_error = merge_error;
        }

        public MergeParamsBean getMerge_params() {
            return merge_params;
        }

        public void setMerge_params(MergeParamsBean merge_params) {
            this.merge_params = merge_params;
        }

        public String getMerge_status() {
            return merge_status;
        }

        public void setMerge_status(String merge_status) {
            this.merge_status = merge_status;
        }

        public Object getMerge_user_id() {
            return merge_user_id;
        }

        public void setMerge_user_id(Object merge_user_id) {
            this.merge_user_id = merge_user_id;
        }

        public boolean isMerge_when_pipeline_succeeds() {
            return merge_when_pipeline_succeeds;
        }

        public void setMerge_when_pipeline_succeeds(boolean merge_when_pipeline_succeeds) {
            this.merge_when_pipeline_succeeds = merge_when_pipeline_succeeds;
        }

        public Object getMilestone_id() {
            return milestone_id;
        }

        public void setMilestone_id(Object milestone_id) {
            this.milestone_id = milestone_id;
        }

        public String getSource_branch() {
            return source_branch;
        }

        public void setSource_branch(String source_branch) {
            this.source_branch = source_branch;
        }

        public int getSource_project_id() {
            return source_project_id;
        }

        public void setSource_project_id(int source_project_id) {
            this.source_project_id = source_project_id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTarget_branch() {
            return target_branch;
        }

        public void setTarget_branch(String target_branch) {
            this.target_branch = target_branch;
        }

        public int getTarget_project_id() {
            return target_project_id;
        }

        public void setTarget_project_id(int target_project_id) {
            this.target_project_id = target_project_id;
        }

        public int getTime_estimate() {
            return time_estimate;
        }

        public void setTime_estimate(int time_estimate) {
            this.time_estimate = time_estimate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public Object getUpdated_by_id() {
            return updated_by_id;
        }

        public void setUpdated_by_id(Object updated_by_id) {
            this.updated_by_id = updated_by_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public SourceBean getSource() {
            return source;
        }

        public void setSource(SourceBean source) {
            this.source = source;
        }

        public TargetBean getTarget() {
            return target;
        }

        public void setTarget(TargetBean target) {
            this.target = target;
        }

        public LastCommitBean getLast_commit() {
            return last_commit;
        }

        public void setLast_commit(LastCommitBean last_commit) {
            this.last_commit = last_commit;
        }

        public boolean isWork_in_progress() {
            return work_in_progress;
        }

        public void setWork_in_progress(boolean work_in_progress) {
            this.work_in_progress = work_in_progress;
        }

        public int getTotal_time_spent() {
            return total_time_spent;
        }

        public void setTotal_time_spent(int total_time_spent) {
            this.total_time_spent = total_time_spent;
        }

        public Object getHuman_total_time_spent() {
            return human_total_time_spent;
        }

        public void setHuman_total_time_spent(Object human_total_time_spent) {
            this.human_total_time_spent = human_total_time_spent;
        }

        public Object getHuman_time_estimate() {
            return human_time_estimate;
        }

        public void setHuman_time_estimate(Object human_time_estimate) {
            this.human_time_estimate = human_time_estimate;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getOldrev() {
            return oldrev;
        }

        public void setOldrev(String oldrev) {
            this.oldrev = oldrev;
        }

        public static class MergeParamsBean {
            /**
             * force_remove_source_branch : 0
             */

            private String force_remove_source_branch;

            public String getForce_remove_source_branch() {
                return force_remove_source_branch;
            }

            public void setForce_remove_source_branch(String force_remove_source_branch) {
                this.force_remove_source_branch = force_remove_source_branch;
            }
        }

        public static class SourceBean {
            /**
             * id : 1025
             * name : FollowmeAndroidWithComponent
             * description : 组件化架构的android项目
             * web_url : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent
             * avatar_url : null
             * git_ssh_url : git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git
             * git_http_url : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git
             * namespace : jichengzhi
             * visibility_level : 0
             * path_with_namespace : jichengzhi/FollowmeAndroidWithComponent
             * default_branch : master
             * ci_config_path : null
             * homepage : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent
             * url : git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git
             * ssh_url : git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git
             * http_url : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git
             */

            private int id;
            private String name;
            private String description;
            private String web_url;
            private Object avatar_url;
            private String git_ssh_url;
            private String git_http_url;
            private String namespace;
            private int visibility_level;
            private String path_with_namespace;
            private String default_branch;
            private Object ci_config_path;
            private String homepage;
            private String url;
            private String ssh_url;
            private String http_url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getWeb_url() {
                return web_url;
            }

            public void setWeb_url(String web_url) {
                this.web_url = web_url;
            }

            public Object getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(Object avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getGit_ssh_url() {
                return git_ssh_url;
            }

            public void setGit_ssh_url(String git_ssh_url) {
                this.git_ssh_url = git_ssh_url;
            }

            public String getGit_http_url() {
                return git_http_url;
            }

            public void setGit_http_url(String git_http_url) {
                this.git_http_url = git_http_url;
            }

            public String getNamespace() {
                return namespace;
            }

            public void setNamespace(String namespace) {
                this.namespace = namespace;
            }

            public int getVisibility_level() {
                return visibility_level;
            }

            public void setVisibility_level(int visibility_level) {
                this.visibility_level = visibility_level;
            }

            public String getPath_with_namespace() {
                return path_with_namespace;
            }

            public void setPath_with_namespace(String path_with_namespace) {
                this.path_with_namespace = path_with_namespace;
            }

            public String getDefault_branch() {
                return default_branch;
            }

            public void setDefault_branch(String default_branch) {
                this.default_branch = default_branch;
            }

            public Object getCi_config_path() {
                return ci_config_path;
            }

            public void setCi_config_path(Object ci_config_path) {
                this.ci_config_path = ci_config_path;
            }

            public String getHomepage() {
                return homepage;
            }

            public void setHomepage(String homepage) {
                this.homepage = homepage;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getSsh_url() {
                return ssh_url;
            }

            public void setSsh_url(String ssh_url) {
                this.ssh_url = ssh_url;
            }

            public String getHttp_url() {
                return http_url;
            }

            public void setHttp_url(String http_url) {
                this.http_url = http_url;
            }
        }

        public static class TargetBean {
            /**
             * id : 1025
             * name : FollowmeAndroidWithComponent
             * description : 组件化架构的android项目
             * web_url : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent
             * avatar_url : null
             * git_ssh_url : git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git
             * git_http_url : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git
             * namespace : jichengzhi
             * visibility_level : 0
             * path_with_namespace : jichengzhi/FollowmeAndroidWithComponent
             * default_branch : master
             * ci_config_path : null
             * homepage : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent
             * url : git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git
             * ssh_url : git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git
             * http_url : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent.git
             */

            private int id;
            private String name;
            private String description;
            private String web_url;
            private Object avatar_url;
            private String git_ssh_url;
            private String git_http_url;
            private String namespace;
            private int visibility_level;
            private String path_with_namespace;
            private String default_branch;
            private Object ci_config_path;
            private String homepage;
            private String url;
            private String ssh_url;
            private String http_url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getWeb_url() {
                return web_url;
            }

            public void setWeb_url(String web_url) {
                this.web_url = web_url;
            }

            public Object getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(Object avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getGit_ssh_url() {
                return git_ssh_url;
            }

            public void setGit_ssh_url(String git_ssh_url) {
                this.git_ssh_url = git_ssh_url;
            }

            public String getGit_http_url() {
                return git_http_url;
            }

            public void setGit_http_url(String git_http_url) {
                this.git_http_url = git_http_url;
            }

            public String getNamespace() {
                return namespace;
            }

            public void setNamespace(String namespace) {
                this.namespace = namespace;
            }

            public int getVisibility_level() {
                return visibility_level;
            }

            public void setVisibility_level(int visibility_level) {
                this.visibility_level = visibility_level;
            }

            public String getPath_with_namespace() {
                return path_with_namespace;
            }

            public void setPath_with_namespace(String path_with_namespace) {
                this.path_with_namespace = path_with_namespace;
            }

            public String getDefault_branch() {
                return default_branch;
            }

            public void setDefault_branch(String default_branch) {
                this.default_branch = default_branch;
            }

            public Object getCi_config_path() {
                return ci_config_path;
            }

            public void setCi_config_path(Object ci_config_path) {
                this.ci_config_path = ci_config_path;
            }

            public String getHomepage() {
                return homepage;
            }

            public void setHomepage(String homepage) {
                this.homepage = homepage;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getSsh_url() {
                return ssh_url;
            }

            public void setSsh_url(String ssh_url) {
                this.ssh_url = ssh_url;
            }

            public String getHttp_url() {
                return http_url;
            }

            public void setHttp_url(String http_url) {
                this.http_url = http_url;
            }
        }

        public static class LastCommitBean {
            /**
             * id : 962c7fc6487fa815080d955287b7e14cfcdcc9e7
             * message : feat：同步翻译
             * timestamp : 2019-11-05T03:01:04Z
             * url : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent/commit/962c7fc6487fa815080d955287b7e14cfcdcc9e7
             * author : {"name":"muxiannian","email":"muxiannian@followme.cn"}
             */

            private String id;
            private String message;
            private String timestamp;
            private String url;
            private AuthorBean author;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public AuthorBean getAuthor() {
                return author;
            }

            public void setAuthor(AuthorBean author) {
                this.author = author;
            }

            public static class AuthorBean {
                /**
                 * name : muxiannian
                 * email : muxiannian@followme.cn
                 */

                private String name;
                private String email;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }
            }
        }
    }

    public static class ChangesBean {
        /**
         * assignee : {"previous":null,"current":{"name":"高爽","username":"gaoshuang","avatar_url":"https://secure.gravatar.com/avatar/ae5e5a13c541fb492da552e8afa23b8c?s=80&d=identicon"}}
         * labels : {"previous":[],"current":[{"id":110,"title":"IM-CRM","color":"#F0AD4E","project_id":1025,"created_at":"2019-10-28 10:50:54 UTC","updated_at":"2019-10-28 10:50:54 UTC","template":false,"description":null,"type":"ProjectLabel","group_id":null}]}
         * total_time_spent : {"previous":null,"current":0}
         */

        private AssigneeBean assignee;
        private LabelsBean labels;
        private TotalTimeSpentBean total_time_spent;
        /**
         * state : {"previous":"locked","current":"merged"}
         */

        private StateBean state;

        public AssigneeBean getAssignee() {
            return assignee;
        }

        public void setAssignee(AssigneeBean assignee) {
            this.assignee = assignee;
        }

        public LabelsBean getLabels() {
            return labels;
        }

        public void setLabels(LabelsBean labels) {
            this.labels = labels;
        }

        public TotalTimeSpentBean getTotal_time_spent() {
            return total_time_spent;
        }

        public void setTotal_time_spent(TotalTimeSpentBean total_time_spent) {
            this.total_time_spent = total_time_spent;
        }

        public StateBean getState() {
            return state;
        }

        public void setState(StateBean state) {
            this.state = state;
        }

        public static class AssigneeBean {
            /**
             * previous : null
             * current : {"name":"高爽","username":"gaoshuang","avatar_url":"https://secure.gravatar.com/avatar/ae5e5a13c541fb492da552e8afa23b8c?s=80&d=identicon"}
             */

            private Object previous;
            private CurrentBean current;

            public Object getPrevious() {
                return previous;
            }

            public void setPrevious(Object previous) {
                this.previous = previous;
            }

            public CurrentBean getCurrent() {
                return current;
            }

            public void setCurrent(CurrentBean current) {
                this.current = current;
            }

            public static class CurrentBean {
                /**
                 * name : 高爽
                 * username : gaoshuang
                 * avatar_url : https://secure.gravatar.com/avatar/ae5e5a13c541fb492da552e8afa23b8c?s=80&d=identicon
                 */

                private String name;
                private String username;
                private String avatar_url;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                }
            }
        }

        public static class LabelsBean {
            private List<?> previous;
            private List<CurrentBeanX> current;

            public List<?> getPrevious() {
                return previous;
            }

            public void setPrevious(List<?> previous) {
                this.previous = previous;
            }

            public List<CurrentBeanX> getCurrent() {
                return current;
            }

            public void setCurrent(List<CurrentBeanX> current) {
                this.current = current;
            }

            public static class CurrentBeanX {
                /**
                 * id : 110
                 * title : IM-CRM
                 * color : #F0AD4E
                 * project_id : 1025
                 * created_at : 2019-10-28 10:50:54 UTC
                 * updated_at : 2019-10-28 10:50:54 UTC
                 * template : false
                 * description : null
                 * type : ProjectLabel
                 * group_id : null
                 */

                private int id;
                private String title;
                private String color;
                private int project_id;
                private String created_at;
                private String updated_at;
                private boolean template;
                private Object description;
                private String type;
                private Object group_id;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }

                public int getProject_id() {
                    return project_id;
                }

                public void setProject_id(int project_id) {
                    this.project_id = project_id;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }

                public boolean isTemplate() {
                    return template;
                }

                public void setTemplate(boolean template) {
                    this.template = template;
                }

                public Object getDescription() {
                    return description;
                }

                public void setDescription(Object description) {
                    this.description = description;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public Object getGroup_id() {
                    return group_id;
                }

                public void setGroup_id(Object group_id) {
                    this.group_id = group_id;
                }
            }
        }

        public static class TotalTimeSpentBean {
            /**
             * previous : null
             * current : 0
             */

            private Object previous;
            private int current;

            public Object getPrevious() {
                return previous;
            }

            public void setPrevious(Object previous) {
                this.previous = previous;
            }

            public int getCurrent() {
                return current;
            }

            public void setCurrent(int current) {
                this.current = current;
            }
        }

        public static class StateBean {
            /**
             * previous : locked
             * current : merged
             */

            private String previous;
            private String current;

            public String getPrevious() {
                return previous;
            }

            public void setPrevious(String previous) {
                this.previous = previous;
            }

            public String getCurrent() {
                return current;
            }

            public void setCurrent(String current) {
                this.current = current;
            }
        }
    }

    public static class RepositoryBean {
        /**
         * name : FollowmeAndroidWithComponent
         * url : git@gitlab.followme-internal.com:jichengzhi/FollowmeAndroidWithComponent.git
         * description : 组件化架构的android项目
         * homepage : https://gitlab.followme-internal.com/jichengzhi/FollowmeAndroidWithComponent
         */

        private String name;
        private String url;
        private String description;
        private String homepage;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getHomepage() {
            return homepage;
        }

        public void setHomepage(String homepage) {
            this.homepage = homepage;
        }
    }

    public static class AssigneeBeanX {
        /**
         * name : 高爽
         * username : gaoshuang
         * avatar_url : https://secure.gravatar.com/avatar/ae5e5a13c541fb492da552e8afa23b8c?s=80&d=identicon
         */

        private String name;
        private String username;
        private String avatar_url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }
    }

    public static class LabelsBeanX {
        /**
         * id : 110
         * title : IM-CRM
         * color : #F0AD4E
         * project_id : 1025
         * created_at : 2019-10-28 10:50:54 UTC
         * updated_at : 2019-10-28 10:50:54 UTC
         * template : false
         * description : null
         * type : ProjectLabel
         * group_id : null
         */

        private int id;
        private String title;
        private String color;
        private int project_id;
        private String created_at;
        private String updated_at;
        private boolean template;
        private Object description;
        private String type;
        private Object group_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public boolean isTemplate() {
            return template;
        }

        public void setTemplate(boolean template) {
            this.template = template;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getGroup_id() {
            return group_id;
        }

        public void setGroup_id(Object group_id) {
            this.group_id = group_id;
        }
    }
}