package org.jboss.seam.example.seamspace;

import javax.ejb.Local;

@Local
public interface CommentLocal
{
   void create(MemberBlog blog);
   void save();
   void destroy();
}
