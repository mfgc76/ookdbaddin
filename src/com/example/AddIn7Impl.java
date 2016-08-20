package com.example;

import com.sun.star.uno.XComponentContext;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.lib.uno.helper.WeakBase;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

import kx.c;
import kx.c.KException;
import kx.c.Second;

public final class AddIn7Impl extends WeakBase
   implements com.example.XAddIn7,
              com.sun.star.lang.XServiceInfo,
              com.sun.star.lang.XLocalizable
{
    private final XComponentContext m_xContext;
    private static final String m_implementationName = AddIn7Impl.class.getName();
    private static final String[] m_serviceNames = {
        "com.example.AddIn7" };

    private com.sun.star.lang.Locale m_locale = new com.sun.star.lang.Locale();

    //http://code.kx.com/wiki/Cookbook/InterfacingWithJava#Example_Grid_Viewer_using_Swing
    public static class KxTableModel extends AbstractTableModel {
        private c.Flip flip;
        private c.Dict dict;
        public void setDict(c.Dict data) {
            this.dict = data;
        }
        public void setFlip(c.Flip data) {
            this.flip = data;
        }
        public int getDRowCount() {
            return Array.getLength(dict.x);
        }
        public int getDColumnCount() {
            return Array.getLength(dict.y);
        }
        public int getRowCount() {
            return Array.getLength(flip.y[0]);
        }
        public int getColumnCount() {
            return flip.y.length;
        }
        public Object getValueAt(int rowIndex, int columnIndex) {
            return c.at(flip.y[columnIndex], rowIndex);
        }
        public String getColumnName(int columnIndex) {
            return flip.x[columnIndex];
        }
    };

    public AddIn7Impl( XComponentContext context )
    {
        m_xContext = context;
    }

    public static XSingleComponentFactory __getComponentFactory( String sImplementationName ) {
        XSingleComponentFactory xFactory = null;

        if ( sImplementationName.equals( m_implementationName ) )
            xFactory = Factory.createComponentFactory(AddIn7Impl.class, m_serviceNames);
        return xFactory;
    }

    public static boolean __writeRegistryServiceInfo( XRegistryKey xRegistryKey ) {
        return Factory.writeRegistryServiceInfo(m_implementationName,
                                                m_serviceNames,
                                                xRegistryKey);
    }
    
    // com.example.XAddIn7:
    public Object[][] q(String host, String query, int header, int flip)
    {
        //int[][] ret = new int[][]{{4,3},{2,1}};
        KxTableModel model = new KxTableModel();
        c c = null;
        String lhost;
        String hoststr[];
        hoststr=host.split(":");  //split at ":"  hostname:port:(username:password), user,passwd are optional
        int hd=header; //show table header
        int fp=flip; //rotate list/table
        int nrow,ncol;
        String user,pass;
        
        if(hoststr[0].equals("")) lhost="localhost";
        else lhost=hoststr[0];
        int lport=Integer.parseInt(hoststr[1]);

        if(!query.equals("")){
            try {
                    if(hoststr.length==4){
                        user=hoststr[2];
                        pass=hoststr[3];
                        c = new c(lhost,lport,(user+":"+pass));
                    }
                    else
                        c = new c(lhost,lport);
                    
                    c.tz=TimeZone.getTimeZone("GMT"); //set timezone to gmt
            } catch (Exception ex) {
                    System.err.println (ex);
            }
         Object res;
         String[] strs;
         Date[] dats;
         long[] lngs;
         Second[] scnds;
         double[] dbls;
         //http://stackoverflow.com/questions/21680618/how-to-iterate-through-an-object-that-is-pointing-to-array-of-doubles#21680650         
         Object [][] tmp1=new Object[1][1]; //row x col
         try {
                res= c.k(query);
                //atoms
                if(res instanceof String)
                    tmp1[0][0]=(String)res;
                else if(res instanceof Date)
                    tmp1[0][0]=(Double)((Long) ((Date)(res)).getTime() ).doubleValue();
                else if(res instanceof Long)
                    tmp1[0][0]=(Double)((Long) (res) ).doubleValue();
                else if(res instanceof Second)
                    tmp1[0][0]=(Double)(double)((Second)(res)).i;
                else if(res instanceof Double)
                    tmp1[0][0]=res;
                //lists
                else if(res instanceof String[]){
                  strs=(String[])res;
                  nrow=strs.length;ncol=1;
                  if (fp==1) {int tmp=nrow; nrow=ncol; ncol=tmp; } 
                  Object [][] tmp2=new Object[nrow][ncol]; //row x col
                  for(int i=0;i<strs.length;i++){
                      if (fp==0)tmp2[i][0]=strs[i]; 
                      else tmp2[0][i]=strs[i];
                  }
                  c.close();
                  return tmp2;
                }
                else if(res instanceof Date[]){
                  dats=(Date[])res;
                  nrow=dats.length;ncol=1;
                  if (fp==1) {int tmp=nrow; nrow=ncol; ncol=tmp; } 
                  Object [][] tmp2=new Object[nrow][ncol]; //row x col
                  for(int i=0;i<dats.length;i++){
                      if (fp==0)tmp2[i][0]=(Double)((Long) (dats[i]).getTime() ).doubleValue();
                      else tmp2[0][i]=(Double)((Long) (dats[i]).getTime() ).doubleValue();
                  }
                  c.close();
                  return tmp2;                    
                }
                else if(res instanceof long[]){
                  lngs=(long[])res;
                  nrow=lngs.length;ncol=1;
                  if (fp==1) {int tmp=nrow; nrow=ncol; ncol=tmp; } 
                  Object [][] tmp2=new Object[nrow][ncol]; //row x col
                  for(int i=0;i<lngs.length;i++){
                      if (fp==0)tmp2[i][0]=(Double)(Long.valueOf(i)).doubleValue(); 
                      else tmp2[0][i]=(Double)(Long.valueOf(i)).doubleValue(); 
                  }
                  c.close();
                  return tmp2;                    
                }
                else if(res instanceof Second[]){
                  scnds=(Second[])res;
                  nrow=scnds.length;ncol=1;
                  if (fp==1) {int tmp=nrow; nrow=ncol; ncol=tmp; } 
                  Object [][] tmp2=new Object[nrow][ncol]; //row x col
                  for(int i=0;i<scnds.length;i++){
                      if (fp==0)tmp2[i][0]=(Double)(double)(scnds[i]).i;
                      else tmp2[0][i]=(Double)(double)(scnds[i]).i;
                  }
                  c.close();
                  return tmp2;                    
                }
                else if(res instanceof double[]){
                  dbls=(double[])res;
                  nrow=dbls.length;ncol=1;
                  if (fp==1) {int tmp=nrow; nrow=ncol; ncol=tmp; } 
                  Object [][] tmp2=new Object[nrow][ncol]; //row x col
                  for(int i=0;i<dbls.length;i++){
                      if (fp==0)tmp2[i][0]=(Double)dbls[i];
                      else tmp2[0][i]=(Double)dbls[i];
                  }
                  c.close();
                  return tmp2;                    
                }
                else if(res instanceof c.Dict){ //todo complete...
                        model.setDict((c.Dict) c.k(query));
                        nrow=model.getDRowCount(); //key
                        ncol=model.getDColumnCount(); //value ([] or object[])
                        Object [][] tmp4=new Object[nrow][ncol];
                      /*  for(int i=0;i<nrow-hd;i++){
                        for(int j=0;j<ncol;j++){
                            
                        }
                        }
                       */
                }
                //tables
                else if(res instanceof c.Flip){
                    try {
                        model.setFlip((c.Flip) c.k(query));
                        nrow=model.getRowCount();
                        ncol=model.getColumnCount();

                        //must use 0! if table is keyed
                        if (fp==1) {int tmp=nrow; nrow=ncol; ncol=tmp; } // cannor swap without tmp variable 
                        if(hd==1) {if (fp==0) nrow++; else ncol++; } //include header
                        Object [][] tmp3=new Object[nrow][ncol]; //row x col
                        if(hd==1){
                            for(int i=0;i<(fp==0?ncol:nrow);i++){  //first the header
                            //tmp3[0][i]= model.getColumnName(i);
                                if (fp==0)tmp3[0][i]=model.getColumnName(i);
                                else tmp3[i][0]=     model.getColumnName(i);
                            }
                        }                       
                        if (fp==1) {int tmp=nrow; nrow=ncol; ncol=tmp; } //swap again for assignment
                        for(int i=0;i<nrow-hd;i++){
                        for(int j=0;j<ncol;j++){
                        if(model.getValueAt(i,j) instanceof String){
                        //tmp3[i+hd][j]=model.getValueAt(i,j);
                            if (fp==0)tmp3[i+hd][j]=model.getValueAt(i,j);
                            else tmp3[j][i+hd]=     model.getValueAt(i,j);
                        }
                        else if(model.getValueAt(i,j) instanceof Date){
                        //tmp3[i+hd][j]=(Double)((Long) ((Date)(model.getValueAt(i,j))).getTime() ).doubleValue();
                            if (fp==0)tmp3[i+hd][j]=(Double)((Long) ((Date)(model.getValueAt(i,j))).getTime() ).doubleValue();
                            else tmp3[j][i+hd]=     (Double)((Long) ((Date)(model.getValueAt(i,j))).getTime() ).doubleValue();
                        }
                        else if(model.getValueAt(i,j) instanceof Long){
                        //tmp3[i+hd][j]=(Double)((Long)(model.getValueAt(i,j))).doubleValue();
                            if (fp==0)tmp3[i+hd][j]=(Double)((Long)(model.getValueAt(i,j))).doubleValue();
                            else tmp3[j][i+hd]=     (Double)((Long)(model.getValueAt(i,j))).doubleValue();
                        }
                        else if(model.getValueAt(i,j) instanceof Second){
                        //tmp3[i+hd][j]= (double)((Second)(model.getValueAt(i,j))).i ;
                            if (fp==0)tmp3[i+hd][j]=(double)((Second)(model.getValueAt(i,j))).i;
                            else tmp3[j][i+hd]=     (double)((Second)(model.getValueAt(i,j))).i;
                        }
                        else if(model.getValueAt(i,j) instanceof Double){
                        //tmp3[i+hd][j]=model.getValueAt(i,j);
                            if (fp==0)tmp3[i+hd][j]=model.getValueAt(i,j);
                            else tmp3[j][i+hd]=     model.getValueAt(i,j);
                        }
                        else{
                        //if(qn(model.getValueAt(i,j))==true)
                        //tmp3[i+hd][j]="";   //works with null
                            if (fp==0)tmp3[i+hd][j]="";
                            else tmp3[j][i+hd]=     "";
                        }
                        }
                        }
                        c.close();
                        return tmp3;

                        } catch (KException e1) {
                        } catch (IOException ex) {
                        Logger.getLogger(AddIn7Impl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                }
                c.close();
                return tmp1;
            } catch (KException ex) {
                Logger.getLogger(AddIn7Impl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AddIn7Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                
        return new Object[0][0];
    }

    // com.sun.star.lang.XServiceInfo:
    public String getImplementationName() {
         return m_implementationName;
    }

    public boolean supportsService( String sService ) {
        int len = m_serviceNames.length;

        for( int i=0; i < len; i++) {
            if (sService.equals(m_serviceNames[i]))
                return true;
        }
        return false;
    }

    public String[] getSupportedServiceNames() {
        return m_serviceNames;
    }

    // com.sun.star.lang.XLocalizable:
    public void setLocale(com.sun.star.lang.Locale eLocale)
    {
        m_locale = eLocale;
    }

    public com.sun.star.lang.Locale getLocale()
    {
        return m_locale;
    }

}
/* tests
=q(":5600";"lst2";0;1)
=Q(":5600";"tab";0;1)
=q(":5600";"dic";1;0)
*/