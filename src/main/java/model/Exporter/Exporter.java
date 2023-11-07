package model.Exporter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import controller.ExceptionControllers.ExceptionAlertController;
import model.FundamentalClasses.ShoppingList;

/**
 * Exporter class
 */
public class Exporter {
    private final ShoppingList shoppingList;
    private final File file;

    /**Constructor of the exporter
     * @param sList shopping list to be exported
     * @param f targeted file
     */
    public Exporter(ShoppingList sList, File f){shoppingList=sList; file = f;}

    /**
     * method used to fill the ODT File when exporting
     */
    public void exportSetUp() {
        if (file != null) {
            try (PrintWriter pw = new PrintWriter(file)) {
                List<model.FundamentalClasses.Product> products = shoppingList.getProducts();

                String date = shoppingList.getDate();
                String name = shoppingList.getName();
                pw.println("   " + name + " Shopping List \n" + "Date : " + date);

                products.sort((o1, o2) -> o1.getCategory().compareToIgnoreCase(o2.getCategory()));

                pw.println("Name" + ", " + "Quantity");
                for (model.FundamentalClasses.Product product : products) {
                    pw.println("- " + product.getName() + ", " + product.getQuantity() + " " + product.getUnits() + " " + ": " + product.getCategory());
                }
            } catch (IOException throwables) {
                new ExceptionAlertController().showExceptionWindow(throwables);
            }
        }
    }

    /**
     * Method to get shopping list
     * @return ShoppingList
     */
    public ShoppingList getShoppingList() {
        return shoppingList;
    }


    /**
     * Method to get file
     * @return File
     */
    public File getFile() {
        return file;
    }
}
