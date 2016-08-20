# ookdbaddin
openoffice calc add-in to connect to kdb+

This add-in adds a formula Q() to openoffice calc
to send a command to q. It may return an atom/list/table
or nothing at all.

The parameters are as follows:

=q("hostname:port:username:password";query;header;flip)   

- ; or , separators are used depending on oocalc setting
- username,password are optional,empty host is localhost
- query and host are strings, header and flip numbers
- header: 0:no header, 1:show header if result is table
- flip: 0:no rotation, 1:rotate if result is table or list
- if result is list or table use ctrl+shift+enter, using just
  enter will only show result in current cell - also individual
  cells cannot be modified
- supported time fotmats: time, date, second return milliseconds
  since epoch (UTC), better use formula (E2 / 86400000) + DATE(1970;1;1)
  to convert to oocalc format and format the cell accordingly,
  unsupported formats return empty cell
  
  
Examples

=q(":5200";"5#select from trades where time>=09:30";1;1)
connect to localhost:5200 and get table trades where time>=09:30 with
header and rotated

![<oocalc image>](https://github.com/mfgc76/ookdbaddin/blob/master/img/ookdbimg1.png)

- To run install .oxt file from dist directory
- To build import project into Netbeans
- Built with Java7, tested with kdb+ 3.2, Openoffice 4.11
