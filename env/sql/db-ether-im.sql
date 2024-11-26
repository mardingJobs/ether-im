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

