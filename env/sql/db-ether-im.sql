create table `db-ether-im`.im_single_message
(
    message_id      bigint      not null comment '消息ID'
        primary key,
    sender_id       varchar(32) not null comment '发送者ID',
    sender_terminal varchar(8)  not null comment '发送者终端',
    receiver_id     varchar(32) not null comment '接收者ID',
    content         text        not null comment '消息内容',
    content_type    varchar(8)  not null comment '消息类型',
    status          varchar(32) not null comment '消息状态  ',
    send_time       bigint      not null comment '发送时间',
    create_time     datetime    not null comment '创建时间'
)
    comment '单聊信息表' charset = utf8mb4;

create table im_group
(
    id          bigint auto_increment
        primary key,
    name        varchar(64)                           not null comment '群名称',
    remark      varchar(32) default ''                not null comment '群备注',
    owner_id    varchar(32)                           not null comment '群主',
    creator_id  varchar(32)                           not null comment '创建人ID',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '用户群' charset = utf8mb4;

create table im_group_message
(
    message_id        bigint                             not null
        primary key,
    sender_id         varchar(32)                        not null comment '发送者ID',
    sender_terminal   varchar(8)                         not null comment '发送者终端类型:WEB  APP ',
    group_id          bigint                             not null comment '群ID',
    status            varchar(16)                        not null comment '消息状态：未发送 已发送 已推送 已触达 已撤回 已读',
    content           text                               not null comment '消息内容',
    content_type      varchar(16)                        not null comment '消息内容类型: 文字 图片 文件 语音 视频 ',
    received_user_ids varchar(1024)                      not null comment '已接收消息的用户ID列表',
    send_time         bigint                             not null comment '发送时间',
    create_time       datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '群聊消息表，存放所有群消息' charset = utf8mb4;

create table im_group_user
(
    group_id varchar(32) not null comment '群ID',
    user_id  varchar(32) not null comment '用户ID'
)
    comment '群和用户关系' charset = utf8mb4;


# 群消息拓展表可以删除
create table im_group_message_ext
(
    message_id        bigint        not null comment '消息ID'
        primary key,
    group_id          bigint        not null comment '群ID',
    received_user_ids varchar(1024) not null comment '已接收消息的用户ID列表'
)
    comment '存放未接收的消息';

