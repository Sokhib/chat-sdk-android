package sdk.chat.core.dao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 *
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userThreadLinkMetaValueDaoConfig;
    private final DaoConfig userDaoConfig;
    private final DaoConfig threadMetaValueDaoConfig;
    private final DaoConfig threadDaoConfig;
    private final DaoConfig readReceiptUserLinkDaoConfig;
    private final DaoConfig userMetaValueDaoConfig;
    private final DaoConfig messageDaoConfig;
    private final DaoConfig userThreadLinkDaoConfig;
    private final DaoConfig contactLinkDaoConfig;
    private final DaoConfig messageMetaValueDaoConfig;

    private final UserThreadLinkMetaValueDao userThreadLinkMetaValueDao;
    private final UserDao userDao;
    private final ThreadMetaValueDao threadMetaValueDao;
    private final ThreadDao threadDao;
    private final ReadReceiptUserLinkDao readReceiptUserLinkDao;
    private final UserMetaValueDao userMetaValueDao;
    private final MessageDao messageDao;
    private final UserThreadLinkDao userThreadLinkDao;
    private final ContactLinkDao contactLinkDao;
    private final MessageMetaValueDao messageMetaValueDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userThreadLinkMetaValueDaoConfig = daoConfigMap.get(UserThreadLinkMetaValueDao.class).clone();
        userThreadLinkMetaValueDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        threadMetaValueDaoConfig = daoConfigMap.get(ThreadMetaValueDao.class).clone();
        threadMetaValueDaoConfig.initIdentityScope(type);

        threadDaoConfig = daoConfigMap.get(ThreadDao.class).clone();
        threadDaoConfig.initIdentityScope(type);

        readReceiptUserLinkDaoConfig = daoConfigMap.get(ReadReceiptUserLinkDao.class).clone();
        readReceiptUserLinkDaoConfig.initIdentityScope(type);

        userMetaValueDaoConfig = daoConfigMap.get(UserMetaValueDao.class).clone();
        userMetaValueDaoConfig.initIdentityScope(type);

        messageDaoConfig = daoConfigMap.get(MessageDao.class).clone();
        messageDaoConfig.initIdentityScope(type);

        userThreadLinkDaoConfig = daoConfigMap.get(UserThreadLinkDao.class).clone();
        userThreadLinkDaoConfig.initIdentityScope(type);

        contactLinkDaoConfig = daoConfigMap.get(ContactLinkDao.class).clone();
        contactLinkDaoConfig.initIdentityScope(type);

        messageMetaValueDaoConfig = daoConfigMap.get(MessageMetaValueDao.class).clone();
        messageMetaValueDaoConfig.initIdentityScope(type);

        userThreadLinkMetaValueDao = new UserThreadLinkMetaValueDao(userThreadLinkMetaValueDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);
        threadMetaValueDao = new ThreadMetaValueDao(threadMetaValueDaoConfig, this);
        threadDao = new ThreadDao(threadDaoConfig, this);
        readReceiptUserLinkDao = new ReadReceiptUserLinkDao(readReceiptUserLinkDaoConfig, this);
        userMetaValueDao = new UserMetaValueDao(userMetaValueDaoConfig, this);
        messageDao = new MessageDao(messageDaoConfig, this);
        userThreadLinkDao = new UserThreadLinkDao(userThreadLinkDaoConfig, this);
        contactLinkDao = new ContactLinkDao(contactLinkDaoConfig, this);
        messageMetaValueDao = new MessageMetaValueDao(messageMetaValueDaoConfig, this);

        registerDao(UserThreadLinkMetaValue.class, userThreadLinkMetaValueDao);
        registerDao(User.class, userDao);
        registerDao(ThreadMetaValue.class, threadMetaValueDao);
        registerDao(Thread.class, threadDao);
        registerDao(ReadReceiptUserLink.class, readReceiptUserLinkDao);
        registerDao(UserMetaValue.class, userMetaValueDao);
        registerDao(Message.class, messageDao);
        registerDao(UserThreadLink.class, userThreadLinkDao);
        registerDao(ContactLink.class, contactLinkDao);
        registerDao(MessageMetaValue.class, messageMetaValueDao);
    }
    
    public void clear() {
        userThreadLinkMetaValueDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
        threadMetaValueDaoConfig.clearIdentityScope();
        threadDaoConfig.clearIdentityScope();
        readReceiptUserLinkDaoConfig.clearIdentityScope();
        userMetaValueDaoConfig.clearIdentityScope();
        messageDaoConfig.clearIdentityScope();
        userThreadLinkDaoConfig.clearIdentityScope();
        contactLinkDaoConfig.clearIdentityScope();
        messageMetaValueDaoConfig.clearIdentityScope();
    }

    public UserThreadLinkMetaValueDao getUserThreadLinkMetaValueDao() {
        return userThreadLinkMetaValueDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public ThreadMetaValueDao getThreadMetaValueDao() {
        return threadMetaValueDao;
    }

    public ThreadDao getThreadDao() {
        return threadDao;
    }

    public ReadReceiptUserLinkDao getReadReceiptUserLinkDao() {
        return readReceiptUserLinkDao;
    }

    public UserMetaValueDao getUserMetaValueDao() {
        return userMetaValueDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public UserThreadLinkDao getUserThreadLinkDao() {
        return userThreadLinkDao;
    }

    public ContactLinkDao getContactLinkDao() {
        return contactLinkDao;
    }

    public MessageMetaValueDao getMessageMetaValueDao() {
        return messageMetaValueDao;
    }

}
