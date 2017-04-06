#------------------------------------------------------------
#        Script MySQL.
#------------------------------------------------------------


#------------------------------------------------------------
# Table: Account
#------------------------------------------------------------

CREATE TABLE Account(
        ID        int (11) Auto_increment  NOT NULL ,
        Fname     Varchar (25) ,
        LName     Varchar (25) ,
        Email     Varchar (100) ,
        About     Varchar (500) ,
        showEmail Bool ,
        Picture   Blob ,
        PRIMARY KEY (ID )
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Notification
#------------------------------------------------------------

CREATE TABLE Notification(
        ID              int (11) Auto_increment  NOT NULL ,
        content         Varchar (250) ,
        dateandtime     Date ,
        notifcationDate Date ,
        seen            Bool ,
        ID_Post         Int ,
        ID_Account      Int ,
        PRIMARY KEY (ID )
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Group
#------------------------------------------------------------

CREATE TABLE Group(
        ID           int (11) Auto_increment  NOT NULL ,
        Name         Varchar (25) ,
        creationDate Date ,
        about        Varchar (100) ,
        ID_Account   Int ,
        PRIMARY KEY (ID )
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Conversation
#------------------------------------------------------------

CREATE TABLE Conversation(
        ID int (11) Auto_increment  NOT NULL ,
        PRIMARY KEY (ID )
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Post
#------------------------------------------------------------

CREATE TABLE Post(
        ID          int (11) Auto_increment  NOT NULL ,
        content     Varchar (1000) ,
        Image       Blob ,
        File        Blob ,
        Type        Smallint ,
        postingDate Date ,
        ID_Group    Int ,
        ID_Account  Int ,
        PRIMARY KEY (ID )
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Comment
#------------------------------------------------------------

CREATE TABLE Comment(
        ID         int (11) Auto_increment  NOT NULL ,
        content    Varchar (250) ,
        File       Blob ,
        Type       Smallint ,
        ID_Account Int ,
        ID_Post    Int ,
        PRIMARY KEY (ID )
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Poll
#------------------------------------------------------------

CREATE TABLE Poll(
        ID      int (11) Auto_increment  NOT NULL ,
        Content Varchar (100) ,
        Vote    Smallint ,
        ID_Post Int ,
        PRIMARY KEY (ID )
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Message
#------------------------------------------------------------

CREATE TABLE Message(
        ID              int (11) Auto_increment  NOT NULL ,
        content         Varchar (500) ,
        ID_Conversation Int ,
        ID_Account      Int ,
        PRIMARY KEY (ID )
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: messageNotification
#------------------------------------------------------------

CREATE TABLE messageNotification(
        ID         int (11) Auto_increment  NOT NULL ,
        content    Text ,
        Seen       Bool ,
        ID_Account Int ,
        ID_Message Int ,
        PRIMARY KEY (ID )
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Admin
#------------------------------------------------------------

CREATE TABLE Admin(
        ID       Int NOT NULL ,
        ID_Group Int NOT NULL ,
        PRIMARY KEY (ID ,ID_Group )
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Member
#------------------------------------------------------------

CREATE TABLE Member(
        ID       Int NOT NULL ,
        ID_Group Int NOT NULL ,
        PRIMARY KEY (ID ,ID_Group )
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Participates
#------------------------------------------------------------

CREATE TABLE Participates(
        ID         Int NOT NULL ,
        ID_Account Int NOT NULL ,
        PRIMARY KEY (ID ,ID_Account )
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Votes
#------------------------------------------------------------

CREATE TABLE Votes(
        ID      Int NOT NULL ,
        ID_Poll Int NOT NULL ,
        PRIMARY KEY (ID ,ID_Poll )
)ENGINE=InnoDB;

ALTER TABLE Notification ADD CONSTRAINT FK_Notification_ID_Post FOREIGN KEY (ID_Post) REFERENCES Post(ID);
ALTER TABLE Notification ADD CONSTRAINT FK_Notification_ID_Account FOREIGN KEY (ID_Account) REFERENCES Account(ID);
ALTER TABLE Group ADD CONSTRAINT FK_Group_ID_Account FOREIGN KEY (ID_Account) REFERENCES Account(ID);
ALTER TABLE Post ADD CONSTRAINT FK_Post_ID_Group FOREIGN KEY (ID_Group) REFERENCES Group(ID);
ALTER TABLE Post ADD CONSTRAINT FK_Post_ID_Account FOREIGN KEY (ID_Account) REFERENCES Account(ID);
ALTER TABLE Comment ADD CONSTRAINT FK_Comment_ID_Account FOREIGN KEY (ID_Account) REFERENCES Account(ID);
ALTER TABLE Comment ADD CONSTRAINT FK_Comment_ID_Post FOREIGN KEY (ID_Post) REFERENCES Post(ID);
ALTER TABLE Poll ADD CONSTRAINT FK_Poll_ID_Post FOREIGN KEY (ID_Post) REFERENCES Post(ID);
ALTER TABLE Message ADD CONSTRAINT FK_Message_ID_Conversation FOREIGN KEY (ID_Conversation) REFERENCES Conversation(ID);
ALTER TABLE Message ADD CONSTRAINT FK_Message_ID_Account FOREIGN KEY (ID_Account) REFERENCES Account(ID);
ALTER TABLE messageNotification ADD CONSTRAINT FK_messageNotification_ID_Account FOREIGN KEY (ID_Account) REFERENCES Account(ID);
ALTER TABLE messageNotification ADD CONSTRAINT FK_messageNotification_ID_Message FOREIGN KEY (ID_Message) REFERENCES Message(ID);
ALTER TABLE Admin ADD CONSTRAINT FK_Admin_ID FOREIGN KEY (ID) REFERENCES Account(ID);
ALTER TABLE Admin ADD CONSTRAINT FK_Admin_ID_Group FOREIGN KEY (ID_Group) REFERENCES Group(ID);
ALTER TABLE Member ADD CONSTRAINT FK_Member_ID FOREIGN KEY (ID) REFERENCES Account(ID);
ALTER TABLE Member ADD CONSTRAINT FK_Member_ID_Group FOREIGN KEY (ID_Group) REFERENCES Group(ID);
ALTER TABLE Participates ADD CONSTRAINT FK_Participates_ID FOREIGN KEY (ID) REFERENCES Conversation(ID);
ALTER TABLE Participates ADD CONSTRAINT FK_Participates_ID_Account FOREIGN KEY (ID_Account) REFERENCES Account(ID);
ALTER TABLE Votes ADD CONSTRAINT FK_Votes_ID FOREIGN KEY (ID) REFERENCES Account(ID);
ALTER TABLE Votes ADD CONSTRAINT FK_Votes_ID_Poll FOREIGN KEY (ID_Poll) REFERENCES Poll(ID);
